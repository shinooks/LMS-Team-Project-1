package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 과제 점수 도메인
 * AssignScore 테이블 컬럼 정의
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignScore {

    /**
     * assignScoreId:   PK
     * assignment:      점수를 생성한 과제
     * student:         점수를 받은 학생
     * score:           점수
     * comment:         교수 코멘트
     * visibility:      조회가능여부
     */
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
    private int score; // 점수
    private String comment;
    @Enumerated
    @Column(nullable = false)
    private Visibility visibility;

    public enum Visibility {
        PUBLIC, PRIVATE
    }
}
