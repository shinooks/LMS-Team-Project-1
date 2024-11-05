package com.sesac.backend.entity;

import com.sesac.backend.course.domain.Course;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Syllabus {

    @Id
    @GeneratedValue
    private UUID syllabusId; // 계획서ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course; // 강의ID

    @Column(columnDefinition = "TEXT")
    private String learningObjectives; // 학습목표

    @Column(columnDefinition = "TEXT")
    private String weeklyPlan; // 주차별계획

    @Column(columnDefinition = "TEXT")
    private String evaluationMethods; // 평가방법

    @Column(columnDefinition = "TEXT")
    private String textbooks; // 교재

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일시

    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일시
}
