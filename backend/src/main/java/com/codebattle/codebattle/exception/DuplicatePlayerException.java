package com.codebattle.codebattle.exception;

public class DuplicatePlayerException extends RuntimeException {
    public DuplicatePlayerException(String playerName, String roomCode) {
        super("Player '" + playerName + "' has already joined room " + roomCode);
    }
}
