package com.sesac.backend.evaluation.finalexam.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.Course;
import com.sesac.backend.evaluation.enums.EvaluationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

/**
 * @author dongjin
 * 기말고사 도메인
 * FinalExam 테이블 컬럼 정의
 */
@Getter
@Setter
@ToString(exclude = {"course", "finalExamProblems"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class FinalExam extends BaseEntity {

    /**
     * finalExamId:     PK
     * course:          기말고사 생성한 강의
     * startTime:       시작시간
     * endTime:         종료시간
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalExamId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
    @OneToMany(mappedBy = "finalExam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<FinalExamProblem> finalExamProblems = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationStatus finalExamEvaluationStatus;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
