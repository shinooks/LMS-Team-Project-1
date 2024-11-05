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
    private Assignment assignment; // 과제
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student; // 학생
    @Column(nullable = false)
    private int score; // 점수
    private String comment; // 교수 코멘트
    @Enumerated
    @Column(nullable = false)
    private Visibility visibility; // 조회가능여부

    public enum Visibility {
        PUBLIC, PRIVATE
    }
}
