package com.codebattle.codebattle.service;

import com.codebattle.codebattle.dto.ResultsResponse;
import com.codebattle.codebattle.entity.BattleResult;
import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.repository.BattleResultRepository;
import com.codebattle.codebattle.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultsService {

    private final RoomRepository roomRepository;
    private final BattleResultRepository battleResultRepository;

    public ResultsService(RoomRepository roomRepository, BattleResultRepository battleResultRepository) {
        this.roomRepository = roomRepository;
        this.battleResultRepository = battleResultRepository;
    }

    @Transactional(readOnly = true)
    public ResultsResponse getResults(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalStateException("No room found with code " + roomCode));

        if (room.getStatus() != Room.RoomStatus.COMPLETED) {
            throw new IllegalStateException("This battle hasn't ended yet");
        }

        List<BattleResult> results = battleResultRepository.findByRoom(room);

        List<ResultsResponse.PlayerResult> playerResults = results.stream()
                .map(r -> new ResultsResponse.PlayerResult(
                        r.getPlayer().getUsername(),
                        r.getScore(),
                        r.getQuestionsSolved(),
                        r.getTotalTimeSeconds(),
                        r.isWon()
                ))
                .collect(Collectors.toList());

        boolean draw = results.size() == 2 && results.stream().noneMatch(BattleResult::isWon);
        String winnerUsername = results.stream()
                .filter(BattleResult::isWon)
                .map(r -> r.getPlayer().getUsername())
                .findFirst()
                .orElse(null);

        return new ResultsResponse(room.getRoomCode(), room.getStatus().name(), playerResults, winnerUsername, draw);
    }
}
