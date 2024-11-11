package com.sesac.backend.evaluation.exam.domain;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Correctness;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = {"examSubmit"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"submitId", "number"})
})
public class ExamAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitId", nullable = false)
    private ExamSubmit examSubmit;

    @Column(unique = true, nullable = false)
    private Integer number;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Answer selected = Answer.NOT_SELECTED;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Correctness correctness = Correctness.WRONG;
}
