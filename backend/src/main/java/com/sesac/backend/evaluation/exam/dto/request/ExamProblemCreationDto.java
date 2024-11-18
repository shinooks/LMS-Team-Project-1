package com.sesac.backend.evaluation.exam.dto.request;

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
public class ExamProblemCreationDto {

    private Integer number;
    private Answer correctAnswer;
    private Difficulty difficulty;
    private String question;
    private List<String> choices;
}
