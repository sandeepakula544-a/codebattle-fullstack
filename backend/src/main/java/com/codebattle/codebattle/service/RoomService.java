package com.codebattle.codebattle.service;

import com.codebattle.codebattle.dto.CreateRoomRequest;
import com.codebattle.codebattle.dto.RoomEvent;
import com.codebattle.codebattle.dto.RoomResponse;
import com.codebattle.codebattle.entity.Room;
import com.codebattle.codebattle.entity.RoomPlayer;
import com.codebattle.codebattle.entity.User;
import com.codebattle.codebattle.exception.DuplicatePlayerException;
import com.codebattle.codebattle.exception.RoomFullException;
import com.codebattle.codebattle.exception.RoomNotFoundException;
import com.codebattle.codebattle.repository.RoomPlayerRepository;
import com.codebattle.codebattle.repository.RoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class RoomService {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // no 0/O/1/I ambiguity
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final RoomRepository roomRepository;
    private final RoomPlayerRepository roomPlayerRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final QuestionService questionService;
    private final com.codebattle.codebattle.repository.BattleResultRepository battleResultRepository;

    public RoomService(RoomRepository roomRepository,
                        RoomPlayerRepository roomPlayerRepository,
                        SimpMessagingTemplate messagingTemplate,
                        QuestionService questionService,
                        com.codebattle.codebattle.repository.BattleResultRepository battleResultRepository) {
        this.roomRepository = roomRepository;
        this.roomPlayerRepository = roomPlayerRepository;
        this.messagingTemplate = messagingTemplate;
        this.questionService = questionService;
        this.battleResultRepository = battleResultRepository;
    }

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request, User creator) {
        Room room = new Room();
        room.setRoomCode(generateUniqueRoomCode());
        room.setCreator(creator);
        room.setTopic(request.getTopic());
        room.setNumberOfQuestions(request.getNumberOfQuestions());
        room.setDuration(request.getDuration());
        room.setStatus(Room.RoomStatus.WAITING);

        RoomPlayer creatorPlayer = new RoomPlayer();
        creatorPlayer.setRoom(room);
        creatorPlayer.setUser(creator);
        creatorPlayer.setRole(RoomPlayer.Role.PLAYER_ONE);
        room.getPlayers().add(creatorPlayer);

        Room saved = roomRepository.save(room);
        return RoomResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoom(String roomCode) {
        Room room = findRoomOrThrow(roomCode);
        return RoomResponse.from(room);
    }

    @Transactional
    public RoomResponse joinRoom(String roomCode, User joiningUser) {
        Room room = findRoomOrThrow(roomCode);

        boolean alreadyJoined = room.getPlayers().stream()
                .anyMatch(p -> p.getUser().getId().equals(joiningUser.getId()));
        if (alreadyJoined) {
            throw new DuplicatePlayerException(joiningUser.getUsername(), roomCode);
        }

        int currentPlayerCount = roomPlayerRepository.countByRoom(room);
        if (currentPlayerCount >= 2) {
            throw new RoomFullException(roomCode);
        }

        RoomPlayer player = new RoomPlayer();
        player.setRoom(room);
        player.setUser(joiningUser);
        player.setRole(RoomPlayer.Role.PLAYER_TWO);
        room.getPlayers().add(player);

        // Two players now present -> room is ready for the creator to start.
        room.setStatus(Room.RoomStatus.READY);

        Room saved = roomRepository.save(room);
        RoomResponse response = RoomResponse.from(saved);

        // Notify anyone subscribed to this room's topic (isolated per roomCode).
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomCode,
                new RoomEvent("PLAYER_JOINED", response)
        );

        return response;
    }

    @Transactional
    public RoomResponse startBattle(String roomCode, User requester) {
        Room room = findRoomOrThrow(roomCode);

        if (!room.getCreator().getId().equals(requester.getId())) {
            throw new IllegalStateException("Only the room creator can start the battle");
        }
        if (room.getStatus() != Room.RoomStatus.READY) {
            throw new IllegalStateException("Room is not ready to start (need 2 players, not already started)");
        }

        room.setStatus(Room.RoomStatus.IN_PROGRESS);
        room.setStartedAt(java.time.LocalDateTime.now());

        questionService.assignQuestionsToRoom(room);

        Room saved = roomRepository.save(room);
        RoomResponse response = RoomResponse.from(saved);

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomCode,
                new RoomEvent("BATTLE_STARTED", response)
        );

        return response;
    }

    /**
     * Marks a player as finished with their test.
     * If all players are now finished, the entire battle is completed immediately.
     * Otherwise, a PLAYER_FINISHED event is broadcast to the room.
     */
    @Transactional
    public RoomResponse finishBattle(String roomCode, User requester) {
        Room room = findRoomOrThrow(roomCode);

        boolean isPlayerInRoom = room.getPlayers().stream()
                .anyMatch(p -> p.getUser().getId().equals(requester.getId()));
        if (!isPlayerInRoom) {
            throw new IllegalStateException("You are not a player in this room");
        }

        if (room.getStatus() == Room.RoomStatus.COMPLETED) {
            return RoomResponse.from(room);
        }

        RoomPlayer player = room.getPlayers().stream()
                .filter(p -> p.getUser().getId().equals(requester.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player is not in this room"));

        player.setFinished(true);
        roomPlayerRepository.save(player);

        boolean allFinished = room.getPlayers().stream()
                .allMatch(p -> Boolean.TRUE.equals(p.getFinished()));

        if (allFinished || room.getPlayers().size() < 2) {
            return endBattle(roomCode, requester);
        }

        Room saved = roomRepository.save(room);
        RoomResponse response = RoomResponse.from(saved);

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomCode,
                new RoomEvent("PLAYER_FINISHED", response)
        );

        return response;
    }

    /**
     * Ends the battle: idempotent (calling it again after it's already
     * COMPLETED just returns the current state, doesn't recompute) so it's
     * safe for either player to trigger this, or for it to fire twice if
     * both players' timers hit zero at nearly the same moment.
     */
    @Transactional
    public RoomResponse endBattle(String roomCode, User requester) {
        Room room = findRoomOrThrow(roomCode);

        boolean isPlayerInRoom = room.getPlayers().stream()
                .anyMatch(p -> p.getUser().getId().equals(requester.getId()));
        if (!isPlayerInRoom) {
            throw new IllegalStateException("You are not a player in this room");
        }

        if (room.getStatus() == Room.RoomStatus.COMPLETED) {
            return RoomResponse.from(room); // already ended, nothing to do
        }
        if (room.getStatus() != Room.RoomStatus.IN_PROGRESS) {
            throw new IllegalStateException("This battle isn't in progress");
        }

        room.setStatus(Room.RoomStatus.COMPLETED);
        Room saved = roomRepository.save(room);

        long totalTimeSeconds = room.getStartedAt() == null
                ? 0
                : java.time.Duration.between(room.getStartedAt(), java.time.LocalDateTime.now()).getSeconds();

        RoomPlayer winner = room.getPlayers().stream()
                .max(java.util.Comparator.comparingInt(RoomPlayer::getScore))
                .orElse(null);
        boolean isDraw = room.getPlayers().size() == 2
                && room.getPlayers().get(0).getScore().equals(room.getPlayers().get(1).getScore());

        if (battleResultRepository.findByRoom(room).isEmpty()) {
            for (RoomPlayer rp : room.getPlayers()) {
                com.codebattle.codebattle.entity.BattleResult result = new com.codebattle.codebattle.entity.BattleResult();
                result.setRoom(room);
                result.setPlayer(rp.getUser());
                result.setScore(rp.getScore());
                result.setQuestionsSolved(rp.getScore()); // scoring model: 1 point per distinct question solved
                result.setTotalTimeSeconds(totalTimeSeconds);
                result.setWon(!isDraw && winner != null && winner.getId().equals(rp.getId()));
                battleResultRepository.save(result);
            }
        }

        RoomResponse response = RoomResponse.from(saved);
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomCode,
                new RoomEvent("BATTLE_ENDED", response)
        );

        return response;
    }

    @Transactional(readOnly = true)
    public java.util.List<com.codebattle.codebattle.dto.QuestionResponse> getQuestionsForRoom(String roomCode, User requester) {
        Room room = findRoomOrThrow(roomCode);

        boolean isPlayerInRoom = room.getPlayers().stream()
                .anyMatch(p -> p.getUser().getId().equals(requester.getId()));
        if (!isPlayerInRoom) {
            throw new IllegalStateException("You are not a player in this room");
        }
        if (room.getStatus() == Room.RoomStatus.WAITING || room.getStatus() == Room.RoomStatus.READY) {
            throw new IllegalStateException("The battle hasn't started yet");
        }

        return questionService.getQuestionsForRoom(room);
    }

    private Room findRoomOrThrow(String roomCode) {
        return roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RoomNotFoundException(roomCode));
    }

    private String generateUniqueRoomCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
            }
            code = sb.toString();
        } while (roomRepository.existsByRoomCode(code));
        return code;
    }
}
