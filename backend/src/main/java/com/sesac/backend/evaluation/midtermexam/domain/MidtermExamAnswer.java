package com.sesac.backend.evaluation.midtermexam.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Difficulty;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = {"midtermSubmit"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
@Entity
public class MidtermExamAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID midtermAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midtermSubmitId", nullable = false)
    private MidtermExamSubmit midtermExamSubmit;

    @Column(unique = true, nullable = false)
    private Integer number;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Answer selected;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;
}

