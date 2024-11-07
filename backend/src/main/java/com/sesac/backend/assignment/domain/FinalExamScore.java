package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 기말고사 점수 도메인
 * FinalExamScore 테이블 컬럼 정의
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FinalExamScore {

    /**
     * finalExamScoreId:    PK
     * finalExam:           점수를 생성한 기말고사
     * student:             점수를 받은 학생
     * score:               점수
     * visibility:          조회 가능 여부
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalExamScoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalExamId", nullable = false)
    private FinalExam finalExam;
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
