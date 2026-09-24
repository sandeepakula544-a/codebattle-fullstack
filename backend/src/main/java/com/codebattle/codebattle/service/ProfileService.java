package com.codebattle.codebattle.service;

import com.codebattle.codebattle.dto.ProfileResponse;
import com.codebattle.codebattle.entity.BattleResult;
import com.codebattle.codebattle.entity.User;
import com.codebattle.codebattle.repository.BattleResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final BattleResultRepository battleResultRepository;

    public ProfileService(BattleResultRepository battleResultRepository) {
        this.battleResultRepository = battleResultRepository;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(User user) {
        List<BattleResult> results = battleResultRepository.findByPlayerOrderByIdDesc(user);

        int totalBattles = results.size();
        int wins = (int) results.stream().filter(BattleResult::isWon).count();

        int totalSolved = results.stream().mapToInt(BattleResult::getQuestionsSolved).sum();
        int totalAvailable = results.stream().mapToInt(r -> r.getRoom().getNumberOfQuestions()).sum();
        double accuracy = totalAvailable > 0 ? Math.round(1000.0 * totalSolved / totalAvailable) / 10.0 : 0.0;

        List<ProfileResponse.BattleHistoryEntry> history = results.stream()
                .map(r -> {
                    List<BattleResult> roomResults = battleResultRepository.findByRoom(r.getRoom());
                    boolean draw = roomResults.size() == 2 && roomResults.stream().noneMatch(BattleResult::isWon);
                    return new ProfileResponse.BattleHistoryEntry(
                            r.getRoom().getRoomCode(),
                            r.getRoom().getTopic().name(),
                            r.getScore(),
                            r.getQuestionsSolved(),
                            r.getRoom().getNumberOfQuestions(),
                            r.isWon(),
                            draw
                    );
                })
                .collect(Collectors.toList());

        return new ProfileResponse(user.getUsername(), user.getEmail(), totalBattles, wins, accuracy, history);
    }
}
