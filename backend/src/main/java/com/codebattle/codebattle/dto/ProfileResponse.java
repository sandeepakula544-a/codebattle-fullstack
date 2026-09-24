package com.codebattle.codebattle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private String username;
    private String email;
    private int totalBattles;
    private int wins;
    private double accuracyPercent;
    private List<BattleHistoryEntry> history;

    @Getter
    @AllArgsConstructor
    public static class BattleHistoryEntry {
        private String roomCode;
        private String topic;
        private int score;
        private int questionsSolved;
        private int numberOfQuestions;
        private boolean won;
        private boolean draw;
    }
}
