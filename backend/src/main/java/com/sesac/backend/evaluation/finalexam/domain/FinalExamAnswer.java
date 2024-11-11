package com.sesac.backend.evaluation.finalexam.domain;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Correctness;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = {"finalExamSubmit"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"finalSubmitId", "number"})
})
public class FinalExamAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalSubmitId", nullable = false)
    private FinalExamSubmit finalExamSubmit;

    @Column(unique = true, nullable = false)
    private Integer number;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Answer selected = Answer.NOT_SELECTED;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Correctness correctness = Correctness.WRONG;
}
