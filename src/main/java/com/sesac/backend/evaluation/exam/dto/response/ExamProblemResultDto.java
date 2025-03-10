package com.sesac.backend.evaluation.exam.dto.response;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Correctness;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamProblemResultDto {
    private UUID problemId;
    private Integer number;
    private String question;
    private Correctness correctness;
    private int difficulty;
    private Answer selectedAnswer;
    private Answer correctAnswer;
    private List<String> choices = new ArrayList<>();
}
