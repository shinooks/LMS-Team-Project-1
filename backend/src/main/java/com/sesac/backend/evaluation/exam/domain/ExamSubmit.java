package com.sesac.backend.evaluation.exam.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.enums.SubmissionStatus;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = {"exam", "student", "examAnswers"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ExamSubmit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID submitId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examId", nullable = false)
    private Exam exam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    @OneToMany(mappedBy = "examSubmit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ExamAnswer> examAnswers = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus submissionStatus;
}
