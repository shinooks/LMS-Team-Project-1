package com.sesac.backend.evaluation.finalexam.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.enums.SubmissionStatus;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = {"finalExam", "student", "finalExamAnswers"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class FinalExamSubmit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalSubmitId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalExamId", nullable = false)
    private FinalExam finalExam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    @OneToMany(mappedBy = "finalExamSubmit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FinalExamAnswer> finalExamAnswers = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus submissionStatus;
}
