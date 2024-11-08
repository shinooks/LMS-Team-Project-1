package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;

@Entity
@Table(name = "syllabus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Syllabus extends BaseTimeEntity { // BaseTimeEntity 상속으로 생성/수정 시간 자동화

    @Id
    @GeneratedValue
    private UUID syllabusId; // 계획서ID

    @OneToOne(fetch = FetchType.LAZY, optional = false)  // optional = false 추가
    @JoinColumn(name = "opening_id")  // openingId -> opening_id로 수정
    private CourseOpening courseOpening;  // CourseOpening과의 일대일 관계

    @Column(columnDefinition = "TEXT")
    private String learningObjectives; // 학습목표

    @Column(columnDefinition = "TEXT")
    private String weeklyPlan; // 주차별계획

    @Column(columnDefinition = "TEXT")
    private String evaluationMethod; // 평가방법

    @Column(columnDefinition = "TEXT")
    private String textbooks;  // 교재

    // 연관관계 편의 메서드 추가
    public void setCourseOpening(CourseOpening courseOpening) {
        this.courseOpening = courseOpening;
        courseOpening.setSyllabus(this);  // 양방향 관계 설정
    }
}