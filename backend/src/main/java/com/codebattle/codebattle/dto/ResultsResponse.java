package com.codebattle.codebattle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResultsResponse {
    private String roomCode;
    private String status; // Room.RoomStatus as string - COMPLETED expected
    private List<PlayerResult> players;
    private String winnerUsername; // null if draw
    private boolean draw;

    @Getter
    @AllArgsConstructor
    public static class PlayerResult {
        private String username;
        private int score;
        private int questionsSolved;
        private long totalTimeSeconds;
        private boolean won;
    }
}
