package com.codebattle.codebattle.dto;

import com.codebattle.codebattle.entity.Question;
import com.codebattle.codebattle.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String title;
    private String description;
    private Room.Topic topic;
    private Question.Difficulty difficulty;
    private String starterCode;
    private String examples;
    private String constraints;
    private List<TestCaseResponse> publicTestCases;

    public static QuestionResponse from(Question q, List<TestCaseResponse> publicTestCases) {
        return new QuestionResponse(
                q.getId(),
                q.getTitle(),
                q.getDescription(),
                q.getTopic(),
                q.getDifficulty(),
                q.getStarterCode(),
                q.getExamples(),
                q.getConstraints(),
                publicTestCases
        );
    }
}
