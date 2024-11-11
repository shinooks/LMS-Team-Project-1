package com.sesac.backend.evaluation.finalexam.dto;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Difficulty;
import java.util.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FinalExamProblemDto {

    private UUID finalProblemId;
    private UUID finalExamId;
    private Integer number;
    private Answer correctAnswer;
    private Difficulty difficulty;
    private String question;
    private List<String> choices;
}
