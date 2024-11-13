package com.sesac.backend.grade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GpaCalculationDto {
    // Student 정보는 Score를 통해 가져올 수 있음
    // semester, year는 CourseOpening을 통해 가져옴
    private Double gpa;
    private Integer totalCredits;    // Course의 credits 합계
    private List<GradeDto> grades;
}
