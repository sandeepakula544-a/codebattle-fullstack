package com.codebattle.codebattle.controller;

import com.codebattle.codebattle.dto.RunRequest;
import com.codebattle.codebattle.dto.RunResponse;
import com.codebattle.codebattle.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/execute")
public class ExecutionController {

    private final SubmissionService submissionService;

    public ExecutionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /** "Run Code" - tests against public test cases only, nothing is saved or scored. */
    @PostMapping
    public ResponseEntity<RunResponse> run(@Valid @RequestBody RunRequest request) {
        RunResponse response = submissionService.run(request.getQuestionId(), request.getSourceCode());
        return ResponseEntity.ok(response);
    }
}
