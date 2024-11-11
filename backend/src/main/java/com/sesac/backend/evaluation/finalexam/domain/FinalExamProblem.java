package com.sesac.backend.evaluation.finalexam.domain;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Difficulty;
import jakarta.persistence.*;
import java.util.*;
import lombok.AccessLevel;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = {"finalExam"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"finalExamId", "number"})
})
public class FinalExamProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalExamId", nullable = false)
    private FinalExam finalExam;

    @Column(unique = true, nullable = false)
    private Integer number;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Answer correctAnswer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;
    @Column(nullable = false)
    private String question;
    @ElementCollection
    private List<String> choices = new ArrayList<>();
}
