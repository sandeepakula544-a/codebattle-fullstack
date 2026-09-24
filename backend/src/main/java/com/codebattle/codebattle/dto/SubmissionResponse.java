package com.codebattle.codebattle.dto;

import com.codebattle.codebattle.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionResponse {
    private Submission.Status status;
    private int testsPassed;
    private int testsTotal;
    private Long executionTimeMs;
    private Integer newScore; // the player's updated score in this room, if it changed
}
