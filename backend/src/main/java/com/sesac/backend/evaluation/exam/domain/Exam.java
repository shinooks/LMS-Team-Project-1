package com.sesac.backend.evaluation.exam.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.enums.Type;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

/**
 * @author dongjin
 * 시험 도메인
 * Exam 테이블 컬럼 정의
 */
@Getter
@Setter
@Builder
@ToString(exclude = {"courseOpening", "examProblems"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID examId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ExamProblem> examProblems = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
