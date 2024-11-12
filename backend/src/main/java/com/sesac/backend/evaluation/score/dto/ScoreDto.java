package com.sesac.backend.evaluation.score.dto;

import com.sesac.backend.evaluation.enums.Visibility;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDto {

    private UUID scoreId;
    private UUID assignId;
    private UUID midtermExamId;
    private UUID finalExamId;
    private UUID openingId;
    private UUID studentId;
    private UUID openingId;
    private int assignScore;
    private int midtermExamScore;
    private int finalExamScore;
    private Visibility visibility;

}
