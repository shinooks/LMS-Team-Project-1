package com.sesac.backend.evaluation.exam.dto;

import com.sesac.backend.evaluation.enums.SubmissionStatus;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExamSubmitDto {

    private UUID submitId;
    private UUID examId;
    private UUID studentId;
    private List<UUID> examAnswers;
    private SubmissionStatus submissionStatus;
}
