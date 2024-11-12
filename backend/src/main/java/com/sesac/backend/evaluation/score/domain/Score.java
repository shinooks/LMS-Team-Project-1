package com.sesac.backend.evaluation.score.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.enums.Visibility;
import com.sesac.backend.evaluation.exam.domain.Exam;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
    @JoinColumn(name = "midtermExamId")
    private Exam midtermExam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalExamId")
    private Exam finalExam;
    @OneToOne
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
