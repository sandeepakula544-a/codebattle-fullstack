package com.codebattle.codebattle.service;

import com.codebattle.codebattle.dto.RunResponse;
import com.codebattle.codebattle.dto.SubmissionResponse;
import com.codebattle.codebattle.entity.*;
import com.codebattle.codebattle.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionService {

    private final CodeExecutionService executionService;
    private final QuestionRepository questionRepository;
    private final TestCaseRepository testCaseRepository;
    private final RoomRepository roomRepository;
    private final RoomPlayerRepository roomPlayerRepository;
    private final SubmissionRepository submissionRepository;

    public SubmissionService(CodeExecutionService executionService,
                              QuestionRepository questionRepository,
                              TestCaseRepository testCaseRepository,
                              RoomRepository roomRepository,
                              RoomPlayerRepository roomPlayerRepository,
                              SubmissionRepository submissionRepository) {
        this.executionService = executionService;
        this.questionRepository = questionRepository;
        this.testCaseRepository = testCaseRepository;
        this.roomRepository = roomRepository;
        this.roomPlayerRepository = roomPlayerRepository;
        this.submissionRepository = submissionRepository;
    }

    /** Run Code: compiles + runs against PUBLIC test cases only. Nothing is saved. */
    @Transactional(readOnly = true)
    public RunResponse run(Long questionId, String sourceCode) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("No question found with id " + questionId));
        List<TestCase> publicCases = testCaseRepository.findByQuestionAndHiddenFalse(question);

        Path workDir = null;
        try {
            workDir = executionService.compile(sourceCode);
            CodeExecutionService.CompileResult compileResult = executionService.runCompiler(workDir);

            if (!compileResult.success()) {
                return new RunResponse(false, compileResult.errorMessage(), List.of());
            }

            List<RunResponse.TestCaseResult> results = new ArrayList<>();
            for (TestCase tc : publicCases) {
                CodeExecutionService.TestOutcome outcome =
                        executionService.runTestCase(workDir, tc.getInput(), tc.getExpectedOutput());
                results.add(new RunResponse.TestCaseResult(
                        tc.getInput(), tc.getExpectedOutput(),
                        outcome.timedOut() ? "(timed out)" : (outcome.runtimeError() ? outcome.errorMessage() : outcome.actualOutput()),
                        outcome.passed(), outcome.timedOut(), outcome.runtimeError()
                ));
            }
            return new RunResponse(true, null, results);

        } catch (Exception e) {
            return new RunResponse(false, "Execution error: " + e.getMessage(), List.of());
        } finally {
            executionService.cleanup(workDir);
        }
    }

    /** Submit: compiles + runs against ALL test cases (public + hidden). Persists a Submission and updates score. */
    @Transactional
    public SubmissionResponse submit(String roomCode, Long questionId, String sourceCode, User player) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalStateException("No room found with code " + roomCode));
        if (room.getStatus() != Room.RoomStatus.IN_PROGRESS) {
            throw new IllegalStateException("This battle is not in progress");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("No question found with id " + questionId));
        List<TestCase> allCases = testCaseRepository.findByQuestion(question);

        Path workDir = null;
        long startTime = System.currentTimeMillis();
        Submission.Status status;
        int passed = 0;

        try {
            workDir = executionService.compile(sourceCode);
            CodeExecutionService.CompileResult compileResult = executionService.runCompiler(workDir);

            if (!compileResult.success()) {
                status = Submission.Status.COMPILATION_ERROR;
            } else {
                boolean anyTimeout = false;
                boolean anyRuntimeError = false;

                for (TestCase tc : allCases) {
                    CodeExecutionService.TestOutcome outcome =
                            executionService.runTestCase(workDir, tc.getInput(), tc.getExpectedOutput());
                    if (outcome.passed()) {
                        passed++;
                    } else if (outcome.timedOut()) {
                        anyTimeout = true;
                    } else if (outcome.runtimeError()) {
                        anyRuntimeError = true;
                    }
                }

                if (passed == allCases.size() && !allCases.isEmpty()) {
                    status = Submission.Status.ACCEPTED;
                } else if (anyTimeout) {
                    status = Submission.Status.TIME_LIMIT_EXCEEDED;
                } else if (anyRuntimeError) {
                    status = Submission.Status.RUNTIME_ERROR;
                } else {
                    status = Submission.Status.WRONG_ANSWER;
                }
            }
        } catch (Exception e) {
            status = Submission.Status.RUNTIME_ERROR;
        } finally {
            executionService.cleanup(workDir);
        }

        long executionTimeMs = System.currentTimeMillis() - startTime;

        Submission submission = new Submission();
        submission.setRoom(room);
        submission.setPlayer(player);
        submission.setQuestion(question);
        submission.setSourceCode(sourceCode);
        submission.setStatus(status);
        submission.setExecutionTimeMs(executionTimeMs);
        submissionRepository.save(submission);

        Integer newScore = null;
        if (status == Submission.Status.ACCEPTED) {
            boolean firstTimeSolved = submissionRepository
                    .findByRoomAndPlayerAndQuestionAndStatus(room, player, question, Submission.Status.ACCEPTED)
                    .size() == 1;

            if (firstTimeSolved) {
                RoomPlayer roomPlayer = room.getPlayers().stream()
                        .filter(p -> p.getUser().getId().equals(player.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Player is not in this room"));
                roomPlayer.setScore(roomPlayer.getScore() + 1);
                roomPlayerRepository.save(roomPlayer);
                newScore = roomPlayer.getScore();
            }
        }

        int totalCases = allCases.size();
        return new SubmissionResponse(status, passed, totalCases, executionTimeMs, newScore);
    }
}
