package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 중간고사 점수 도메인
 * MidtermExamScore 테이블 컬럼 정의
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MidtermExamScore {

    /**
     * midtermExamScoreId:      PK
     * midtermExam:             점수를 생성한 중간고사
     * student:                 점수를 받은 학생
     * score:                   점수
     * visibility:              조회 가능 여부
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID midtermExamScoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midtermExamId", nullable = false)
    private MidtermExam midtermExam;
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
