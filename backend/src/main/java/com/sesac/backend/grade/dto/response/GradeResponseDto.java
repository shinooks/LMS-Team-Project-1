package com.sesac.backend.grade.dto.response;

import com.sesac.backend.grade.domain.Grade;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 성적 조회 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GradeResponseDto {
    private Long gradeId;
    private String title;
    private Double assignmentScore;
    private Double midtermScore;
    private Double finalScore;
    private Double totalScore;

    @Builder
    public GradeResponseDto(Grade grade) {
        this.gradeId = grade.getId();
        this.title = grade.getAssignmentDome().getTitle();
        this.assignmentScore = grade.getAssignmentScore();
        this.midtermScore = grade.getMidtermScore();
        this.finalScore = grade.getFinalScore();
        this.totalScore = grade.getScore();
    }
}