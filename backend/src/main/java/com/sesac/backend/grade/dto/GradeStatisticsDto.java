package com.sesac.backend.grade.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GradeStatisticsDto {
    private int totalStudents;      // 수강인원
    private double averageScore;    // 평균점수
    private int highestScore;       // 최고점수
    private int lowestScore;        // 최저점수



}
