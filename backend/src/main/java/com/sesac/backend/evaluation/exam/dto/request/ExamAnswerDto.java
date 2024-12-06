package com.sesac.backend.evaluation.exam.dto.request;

import com.sesac.backend.evaluation.enums.Answer;
import java.util.UUID;
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
public class ExamAnswerDto {
    private UUID problemId;
    private Integer number;
    private Answer selectedAnswer;
}
