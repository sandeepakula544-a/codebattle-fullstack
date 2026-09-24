package com.codebattle.codebattle.controller;

import com.codebattle.codebattle.dto.CreateRoomRequest;
import com.codebattle.codebattle.dto.RoomResponse;
import com.codebattle.codebattle.entity.User;
import com.codebattle.codebattle.repository.UserRepository;
import com.codebattle.codebattle.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final UserRepository userRepository;

    public RoomController(RoomService roomService, UserRepository userRepository) {
        this.roomService = roomService;
        this.userRepository = userRepository;
    }

    /** Create a new room. The authenticated caller automatically becomes Player 1. */
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request,
                                                    Authentication authentication) {
        User user = currentUser(authentication);
        RoomResponse response = roomService.createRoom(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Fetch a room's current state by its code. */
    @GetMapping("/{roomCode}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable String roomCode) {
        RoomResponse response = roomService.getRoom(roomCode.toUpperCase());
        return ResponseEntity.ok(response);
    }

    /** Join an existing room as Player 2. The authenticated caller is the joining player. */
    @PostMapping("/{roomCode}/join")
    public ResponseEntity<RoomResponse> joinRoom(@PathVariable String roomCode,
                                                  Authentication authentication) {
        User user = currentUser(authentication);
        RoomResponse response = roomService.joinRoom(roomCode.toUpperCase(), user);
        return ResponseEntity.ok(response);
    }

    /** Start the battle. Creator-only, enforced server-side regardless of what the UI shows. */
    @PostMapping("/{roomCode}/start")
    public ResponseEntity<RoomResponse> startBattle(@PathVariable String roomCode,
                                                      Authentication authentication) {
        User user = currentUser(authentication);
        RoomResponse response = roomService.startBattle(roomCode.toUpperCase(), user);
        return ResponseEntity.ok(response);
    }

    /** Ends the battle and finalizes results. Either player can call this (e.g. when their timer hits zero). */
    @PostMapping("/{roomCode}/end")
    public ResponseEntity<RoomResponse> endBattle(@PathVariable String roomCode,
                                                    Authentication authentication) {
        User user = currentUser(authentication);
        RoomResponse response = roomService.endBattle(roomCode.toUpperCase(), user);
        return ResponseEntity.ok(response);
    }

    /** Questions assigned to this room, fixed order, same for both players. Only players in the room can fetch them. */
    @GetMapping("/{roomCode}/questions")
    public ResponseEntity<java.util.List<com.codebattle.codebattle.dto.QuestionResponse>> getRoomQuestions(
            @PathVariable String roomCode, Authentication authentication) {
        User user = currentUser(authentication);
        var response = roomService.getQuestionsForRoom(roomCode.toUpperCase(), user);
        return ResponseEntity.ok(response);
    }

    /**
     * The JWT filter authenticates by username only (see AppUserDetailsService), so we
     * look the full User entity up here rather than trusting anything from the request body.
     */
    private User currentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + username));
    }
}
