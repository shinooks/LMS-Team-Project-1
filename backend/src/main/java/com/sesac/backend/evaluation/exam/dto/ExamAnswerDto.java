package com.sesac.backend.evaluation.exam.dto;

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
public class ExamAnswerDto {

    private UUID answerId;
    private UUID submitId;
    private Integer number;
    private Answer selected;
    private Correctness correctness;
}
