package com.sesac.backend.evaluation.exam.dto.response;

import java.time.LocalDateTime;
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
public class ExamReadResponse {

    private UUID examId;
    private String title;
    private String courseName;
    private String duration;
    private List<ExamProblemReadDto> questions;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalQuestions;
}
