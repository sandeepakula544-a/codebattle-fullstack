package com.codebattle.codebattle.dto;

import com.codebattle.codebattle.entity.TestCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestCaseResponse {
    private String input;
    private String expectedOutput;

    public static TestCaseResponse from(TestCase testCase) {
        return new TestCaseResponse(testCase.getInput(), testCase.getExpectedOutput());
    }
}
