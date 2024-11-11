package com.sesac.backend.evaluation.finalexam.dto;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Correctness;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FinalExamAnswerDto {

    private UUID finalExamId;
    private UUID finalExamSubmitId;
    private Integer number;
    private Answer selected;
    private Correctness correctness;
}
