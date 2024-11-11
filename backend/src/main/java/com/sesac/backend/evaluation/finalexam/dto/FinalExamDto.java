package com.sesac.backend.evaluation.finalexam.dto;

import com.sesac.backend.evaluation.enums.EvaluationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 기말고사 dto
 * FinalExam 데이터 전달 객체
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FinalExamDto {

    /**
     * finalExamId:     PK
     * course:          기말고사 생성한 강의
     * startTime:       시작시간
     * endTime:         종료시간
     */
    private UUID finalExamId;
    private UUID courseId;
    private List<UUID> finalExamProblems;
    private EvaluationStatus evaluationStatus;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
