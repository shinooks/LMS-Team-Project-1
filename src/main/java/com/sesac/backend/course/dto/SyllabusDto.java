package com.sesac.backend.course.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusDto {
    private UUID syllabusId;  // 계획서 ID (고유 식별자)

    private String learningObjectives;  // 학습목표 (강의 학습 목표)
    private String weeklyPlan;          // 주차별 강의 계획
    private String evaluationMethod;     // 평가 방법 (시험, 과제 등)
    private String textbooks;            // 교재 및 참고문헌
}