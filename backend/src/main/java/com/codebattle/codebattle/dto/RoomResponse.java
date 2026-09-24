package com.codebattle.codebattle.dto;

import com.codebattle.codebattle.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class RoomResponse {
    private String roomCode;
    private String creatorName;
    private Room.Topic topic;
    private Integer numberOfQuestions;
    private Integer duration;
    private Room.RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private List<PlayerResponse> players;

    public static RoomResponse from(Room room) {
        List<PlayerResponse> players = room.getPlayers().stream()
                .map(PlayerResponse::from)
                .collect(Collectors.toList());

        return new RoomResponse(
                room.getRoomCode(),
                room.getCreator().getUsername(),
                room.getTopic(),
                room.getNumberOfQuestions(),
                room.getDuration(),
                room.getStatus(),
                room.getCreatedAt(),
                room.getStartedAt(),
                players
        );
    }
}
