package com.sesac.backend.evaluation.score.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.enums.Visibility;
import com.sesac.backend.evaluation.exam.domain.Exam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Score extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID scoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignId")
    private Assignment assignment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midtermExamId", unique = true)
    private Exam midtermExam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalExamId", unique = true)
    private Exam finalExam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private Student student;
    @Column(nullable = false)
    private int assignScore;
    @Column(nullable = false)
    private int midtermExamScore;
    @Column(nullable = false)
    private int finalExamScore;
    @Enumerated
    @Column(nullable = false)
    private Visibility visibility;
}
