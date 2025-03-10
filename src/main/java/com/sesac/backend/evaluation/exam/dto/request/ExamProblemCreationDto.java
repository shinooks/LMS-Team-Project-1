package com.sesac.backend.evaluation.exam.dto.request;

import com.sesac.backend.evaluation.enums.Answer;
import com.sesac.backend.evaluation.enums.Difficulty;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExamProblemCreationDto {

    private Integer number;
    private String question;
    private Difficulty difficulty;
    private Answer correctAnswer;
    private List<String> choices;
}
