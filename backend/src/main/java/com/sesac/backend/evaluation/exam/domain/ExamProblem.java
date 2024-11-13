package com.sesac.backend.evaluation.exam.domain;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Correctness;
import com.sesac.backend.evaluation.enums.Difficulty;
import jakarta.persistence.*;
import java.util.*;
import lombok.AccessLevel;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = {"exam"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"examId", "number"})
})
public class ExamProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examId", nullable = false)
    private Exam exam;

    @Column(unique = true, nullable = false)
    private Integer number;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Answer correctAnswer;
    @Enumerated(EnumType.STRING)
    @Column()
    private Answer selectedAnswer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Correctness correctness = Correctness.WRONG;
    @Column(nullable = false)
    private String question;
    @ElementCollection
    private List<String> choices = new ArrayList<>();
}
