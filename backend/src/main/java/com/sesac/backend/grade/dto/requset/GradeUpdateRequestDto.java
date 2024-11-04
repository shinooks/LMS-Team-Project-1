package com.sesac.backend.grade.dto.requset;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GradeUpdateRequestDto {

    @NotNull(message = "과제 점수를 입력해주세요")
    @Min(value = 0, message = "점수는 0점 이상이어야 합니다")
    @Max(value = 100, message = "점수는 100점 이하여야 합니다")
    private Double assignmentScore;

    @NotNull(message = "중간고사 점수를 입력해주세요")
    @Min(value = 0)
    @Max(value = 100)
    private Double midtermScore;

    @NotNull(message = "기말고사 점수를 입력해주세요")
    @Min(value = 0)
    @Max(value = 100)
    private Double finalScore;

    @Builder
    public GradeUpdateRequestDto(Double assignmentScore, Double midtermScore, Double finalScore) {
        this.assignmentScore = assignmentScore;
        this.midtermScore = midtermScore;
        this.finalScore = finalScore;
    }
}