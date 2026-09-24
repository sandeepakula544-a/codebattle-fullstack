package com.codebattle.codebattle.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private List<String> messages;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String error, String message) {
        this(status, error, List.of(message), LocalDateTime.now());
    }
}
