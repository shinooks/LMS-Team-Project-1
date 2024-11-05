package com.sesac.backend.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignScore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assignScoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignId", nullable = false)
    private Assignment assignment;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    @Column(nullable = false)
    private int score;
    @Enumerated
    @Column(nullable = false)
    private Visibility visibility;

    public enum Visibility {
        PUBLIC, PRIVATE
    }
}
