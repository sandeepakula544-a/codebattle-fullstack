package com.codebattle.codebattle.controller;

import com.codebattle.codebattle.dto.SubmissionRequest;
import com.codebattle.codebattle.dto.SubmissionResponse;
import com.codebattle.codebattle.entity.User;
import com.codebattle.codebattle.repository.UserRepository;
import com.codebattle.codebattle.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final UserRepository userRepository;

    public SubmissionController(SubmissionService submissionService, UserRepository userRepository) {
        this.submissionService = submissionService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<SubmissionResponse> submit(@Valid @RequestBody SubmissionRequest request,
                                                       Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
        SubmissionResponse response = submissionService.submit(
                request.getRoomCode().toUpperCase(), request.getQuestionId(), request.getSourceCode(), user);
        return ResponseEntity.ok(response);
    }
}
