package com.codebattle.codebattle.service;

import com.codebattle.codebattle.dto.LeaderboardEntryResponse;
import com.codebattle.codebattle.entity.BattleResult;
import com.codebattle.codebattle.entity.User;
import com.codebattle.codebattle.repository.BattleResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private final BattleResultRepository battleResultRepository;

    public LeaderboardService(BattleResultRepository battleResultRepository) {
        this.battleResultRepository = battleResultRepository;
    }

    /**
     * "Accuracy" here means questions solved / questions offered, across all
     * of a player's battles. "Avg solving time" is average battle duration
     * (see BattleResult.totalTimeSeconds javadoc) - not per-question speed,
     * since we don't track that separately yet.
     */
    @Transactional(readOnly = true)
    public List<LeaderboardEntryResponse> getLeaderboard() {
        List<BattleResult> all = battleResultRepository.findAll();

        Map<Long, List<BattleResult>> byPlayer = all.stream()
                .collect(Collectors.groupingBy(r -> r.getPlayer().getId()));

        List<LeaderboardEntryResponse> unranked = new ArrayList<>();

        for (List<BattleResult> results : byPlayer.values()) {
            User player = results.get(0).getPlayer();
            int battlesPlayed = results.size();
            int battlesWon = (int) results.stream().filter(BattleResult::isWon).count();

            int totalSolved = results.stream().mapToInt(BattleResult::getQuestionsSolved).sum();
            int totalAvailable = results.stream()
                    .mapToInt(r -> r.getRoom().getNumberOfQuestions())
                    .sum();
            double accuracy = totalAvailable > 0 ? (100.0 * totalSolved / totalAvailable) : 0.0;

            double avgTime = results.stream()
                    .mapToLong(BattleResult::getTotalTimeSeconds)
                    .average()
                    .orElse(0.0);

            unranked.add(new LeaderboardEntryResponse(
                    0, player.getUsername(), battlesPlayed, battlesWon,
                    Math.round(accuracy * 10) / 10.0, Math.round(avgTime * 10) / 10.0
            ));
        }

        unranked.sort(
                Comparator.comparingInt(LeaderboardEntryResponse::getBattlesWon).reversed()
                        .thenComparing(Comparator.comparingDouble(LeaderboardEntryResponse::getAccuracyPercent).reversed())
        );

        List<LeaderboardEntryResponse> ranked = new ArrayList<>();
        for (int i = 0; i < unranked.size(); i++) {
            LeaderboardEntryResponse e = unranked.get(i);
            ranked.add(new LeaderboardEntryResponse(
                    i + 1, e.getUsername(), e.getBattlesPlayed(), e.getBattlesWon(),
                    e.getAccuracyPercent(), e.getAvgSolvingTimeSeconds()
            ));
        }
        return ranked;
    }
}
