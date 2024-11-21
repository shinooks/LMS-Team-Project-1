package com.sesac.backend.evaluation.score.dto;

import com.sesac.backend.evaluation.enums.Type;
import com.sesac.backend.evaluation.exam.dto.response.ExamProblemResultDto;
import java.time.LocalDateTime;
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
public class ScoreDetailResponse {
    private UUID examId;
    private String title;
    private Type type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int midtermExamScore;
    private int finalExamScore;
    private List<ExamProblemResultDto> examProblems;
}
