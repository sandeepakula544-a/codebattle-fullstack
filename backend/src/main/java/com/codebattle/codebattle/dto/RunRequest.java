package com.codebattle.codebattle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunRequest {

    @NotNull(message = "questionId is required")
    private Long questionId;

    @NotBlank(message = "sourceCode is required")
    private String sourceCode;
}
