package com.codebattle.codebattle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Written once, when a room's battle ends (see RoomService.endBattle).
 * One row per player per room. This is the permanent record the
 * Leaderboard and Profile pages read from.
 */
@Entity
@Table(name = "battle_results")
@Getter
@Setter
@NoArgsConstructor
public class BattleResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "questions_solved", nullable = false)
    private Integer questionsSolved;

    /**
     * Seconds from room.startedAt to when the battle ended. NOTE: this is
     * the whole battle's duration, the same value for both players - we do
     * not currently track each player's individual first-accepted-submission
     * time per question, so "average solving time" on the leaderboard is
     * really "average battle duration", not per-question speed. Documented
     * here so this isn't mistaken for more precise data than it is.
     */
    @Column(name = "total_time_seconds", nullable = false)
    private Long totalTimeSeconds;

    @Column(nullable = false)
    private boolean won = false;
}
