package com.sesac.backend.evaluation.exam.dto;

import com.sesac.backend.evaluation.enums.Answer;
import java.util.List;
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
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExamSubmissionRequest {

    private UUID examId;
    private UUID studentId;
    private List<ExamAnswerDto> answers;
}
