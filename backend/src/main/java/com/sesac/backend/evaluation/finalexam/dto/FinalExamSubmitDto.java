package com.sesac.backend.evaluation.finalexam.dto;

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
public class FinalExamSubmitDto {

    private UUID finalSubmitId;
    private UUID finalExamId;
    private UUID studentId;
    private List<UUID> finalExamAnswers;
    private SubmissionStatus submissionStatus;
}
