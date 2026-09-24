package com.codebattle.codebattle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Both players in a room must see the exact same questions in the exact
 * same order (spec requirement). This join entity is written once, when
 * the battle starts, so the assignment is fixed server-side and can't
 * drift between the two players' clients.
 */
@Entity
@Table(name = "room_questions")
@Getter
@Setter
@NoArgsConstructor
public class RoomQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}
