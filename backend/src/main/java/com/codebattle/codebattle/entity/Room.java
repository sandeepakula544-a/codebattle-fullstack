package com.codebattle.codebattle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Short, human-shareable code e.g. "A1B2C3". Unique across all rooms. */
    @Column(name = "room_code", nullable = false, unique = true, length = 12)
    private String roomCode;

    /** The authenticated user who created the room. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Topic topic;

    @Column(name = "number_of_questions", nullable = false)
    private Integer numberOfQuestions;

    /** Duration of the battle in minutes. */
    @Column(nullable = false)
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.WAITING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /** Server-side authoritative timestamp — set only when the creator starts the battle. */
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPlayer> players = new ArrayList<>();

    public enum Topic {
        ARRAYS, STRINGS, LINKED_LIST, TREES, GRAPHS
    }

    public enum RoomStatus {
        WAITING,     // room created, waiting for player 2
        READY,       // 2 players present, not started yet
        IN_PROGRESS, // battle started
        COMPLETED    // battle finished
    }
}
