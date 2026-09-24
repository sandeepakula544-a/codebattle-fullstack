package com.codebattle.codebattle.exception;

public class RoomFullException extends RuntimeException {
    public RoomFullException(String roomCode) {
        super("Room " + roomCode + " already has 2 players");
    }
}
