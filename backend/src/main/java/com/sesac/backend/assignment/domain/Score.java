package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID scoreId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignId", nullable = false)
    private Assignment assignment;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midtermExamId", nullable = false)
    private MidtermExam midtermExam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalExamId", nullable = false)
    private FinalExam finalExam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="openingId" , nullable = false)
    private CourseOpening courseOpening;

    @OneToOne
    @JoinColumn(name = "studentId", nullable = false)
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

    public enum Visibility {
        PUBLIC, PRIVATE
    }
}
