package com.codebattle.codebattle.dto;

import com.codebattle.codebattle.entity.RoomPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerResponse {
    private String playerName;
    private RoomPlayer.Role role;
    private Integer score;
    private Boolean finished;

    public static PlayerResponse from(RoomPlayer player) {
        return new PlayerResponse(
                player.getUser().getUsername(),
                player.getRole(),
                player.getScore(),
                player.getFinished() != null ? player.getFinished() : false
        );
    }
}
