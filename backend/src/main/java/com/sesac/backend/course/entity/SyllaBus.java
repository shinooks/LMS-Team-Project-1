package com.sesac.backend.course.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "강의계획서")
@Getter @Setter
@NoArgsConstructor
public class SyllaBus {
    @Id
    @Column(name = "계획서ID")
    private String syllabusId;

    @Column(name = "개설ID")
    private String openingId;

    @Column(name = "학습목표")
    private String learningObjectives;

    @Column(name = "주차별계획")
    @Lob    // 대용량 텍스트를 위한 어노테이션
    private String weeklyPlan;

    @Column(name = "평가방법")
    private String evaluationMethod;

    @Column(name = "교재")
    private String textbook;

    @OneToOne
    @JoinColumn(name = "개설ID", insertable = false, updatable = false)
    private CourseOpening courseOpening;
}