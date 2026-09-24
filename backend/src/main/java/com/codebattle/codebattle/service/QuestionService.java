package com.codebattle.codebattle.service;

import com.codebattle.codebattle.dto.QuestionResponse;
import com.codebattle.codebattle.dto.TestCaseResponse;
import com.codebattle.codebattle.entity.Question;
import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.entity.RoomQuestion;
import com.codebattle.codebattle.repository.QuestionRepository;
import com.codebattle.codebattle.repository.RoomQuestionRepository;
import com.codebattle.codebattle.repository.TestCaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TestCaseRepository testCaseRepository;
    private final RoomQuestionRepository roomQuestionRepository;

    public QuestionService(QuestionRepository questionRepository,
                            TestCaseRepository testCaseRepository,
                            RoomQuestionRepository roomQuestionRepository) {
        this.questionRepository = questionRepository;
        this.testCaseRepository = testCaseRepository;
        this.roomQuestionRepository = roomQuestionRepository;
    }

    /**
     * Picks `room.numberOfQuestions` random questions matching the room's topic
     * and fixes their order for this room. Called once, when the battle starts,
     * so both players are guaranteed to see the exact same set in the exact
     * same order (they read it back via getQuestionsForRoom).
     */
    @Transactional
    public void assignQuestionsToRoom(Room room) {
        List<Question> pool = questionRepository.findByTopic(room.getTopic());
        Collections.shuffle(pool);

        int count = Math.min(room.getNumberOfQuestions(), pool.size());
        List<Question> selected = pool.subList(0, count);

        for (int i = 0; i < selected.size(); i++) {
            RoomQuestion rq = new RoomQuestion();
            rq.setRoom(room);
            rq.setQuestion(selected.get(i));
            rq.setOrderIndex(i);
            roomQuestionRepository.save(rq);
        }
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsForRoom(Room room) {
        return roomQuestionRepository.findByRoomOrderByOrderIndexAsc(room).stream()
                .map(RoomQuestion::getQuestion)
                .map(this::toPublicResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::toPublicResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No question found with id " + id));
        return toPublicResponse(question);
    }

    private QuestionResponse toPublicResponse(Question question) {
        List<TestCaseResponse> publicCases = testCaseRepository.findByQuestionAndHiddenFalse(question).stream()
                .map(TestCaseResponse::from)
                .collect(Collectors.toList());
        return QuestionResponse.from(question, publicCases);
    }
}
