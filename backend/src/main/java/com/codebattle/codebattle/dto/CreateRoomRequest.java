package com.codebattle.codebattle.dto;

import com.codebattle.codebattle.entity.Room;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequest {

    // No playerName field: the creator is always the authenticated user
    // (taken from the JWT in the request), never client-supplied text.

    @NotNull(message = "Topic is required")
    private Room.Topic topic;

    @NotNull(message = "Number of questions is required")
    @Min(value = 1, message = "There must be at least 1 question")
    @Max(value = 10, message = "There can be at most 10 questions")
    private Integer numberOfQuestions;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 180, message = "Duration must be at most 180 minutes")
    private Integer duration;
}
