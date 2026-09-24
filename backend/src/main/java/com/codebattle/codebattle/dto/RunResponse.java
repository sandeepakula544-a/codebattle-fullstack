package com.codebattle.codebattle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RunResponse {
    private boolean compiled;
    private String compileError; // null if compiled
    private List<TestCaseResult> results;

    @Getter
    @AllArgsConstructor
    public static class TestCaseResult {
        private String input;
        private String expectedOutput;
        private String actualOutput;
        private boolean passed;
        private boolean timedOut;
        private boolean runtimeError;
    }
}
