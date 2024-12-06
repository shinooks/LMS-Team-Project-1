package com.sesac.backend.evaluation.score.dto;

import com.sesac.backend.evaluation.enums.Visibility;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private int assignScore;
    private int midtermExamScore;
    private int finalExamScore;
    private Visibility visibility;

}
