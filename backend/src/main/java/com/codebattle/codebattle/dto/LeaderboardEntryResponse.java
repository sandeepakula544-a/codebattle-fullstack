package com.codebattle.codebattle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardEntryResponse {
    private int rank;
    private String username;
    private int battlesPlayed;
    private int battlesWon;
    private double accuracyPercent;
    private double avgSolvingTimeSeconds;
}
