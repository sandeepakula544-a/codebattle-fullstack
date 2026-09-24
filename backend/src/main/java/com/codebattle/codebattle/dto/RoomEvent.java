package com.codebattle.codebattle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Broadcast to /topic/room/{roomCode} whenever room state changes
 * (a player joins, the battle starts, etc). The frontend re-fetches
 * or updates its local state based on the "type" field.
 */
@Getter
@AllArgsConstructor
public class RoomEvent {
    private String type; // e.g. "PLAYER_JOINED", "BATTLE_STARTED"
    private RoomResponse room;
}
