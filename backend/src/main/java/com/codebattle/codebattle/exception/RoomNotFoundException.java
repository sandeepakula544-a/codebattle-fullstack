package com.codebattle.codebattle.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String roomCode) {
        super("No room found with code: " + roomCode);
    }
}
