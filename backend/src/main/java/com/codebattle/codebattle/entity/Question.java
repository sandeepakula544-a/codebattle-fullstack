package com.codebattle.codebattle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NOTE: the original spec's entity list for Question was
 * (id, title, description, topic, difficulty, starterCode). The Coding
 * Contest page requirement also needs Examples and Constraints displayed,
 * so those two fields are added here beyond the minimal list — they're
 * plain text blocks the frontend renders as-is.
 */
@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Room.Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Lob
    @Column(name = "starter_code", nullable = false, columnDefinition = "LONGTEXT")
    private String starterCode;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String examples;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String constraints;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
