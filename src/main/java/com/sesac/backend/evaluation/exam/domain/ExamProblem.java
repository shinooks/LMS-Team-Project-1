package com.sesac.backend.evaluation.exam.domain;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Correctness;
import com.sesac.backend.evaluation.enums.Difficulty;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Column(nullable = false)
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
