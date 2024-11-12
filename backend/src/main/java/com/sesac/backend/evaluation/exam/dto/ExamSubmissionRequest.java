package com.sesac.backend.evaluation.exam.dto;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.*;

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
