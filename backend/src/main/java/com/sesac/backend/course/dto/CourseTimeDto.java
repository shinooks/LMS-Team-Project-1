package com.sesac.backend.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sesac.backend.course.constant.DayOfWeek;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseTimeDto {
    private UUID timeId;    // 시간ID (자동생성)

    @NotNull(message = "강의 개설 ID는 필수입니다")
    private UUID openingId;  // 강의 개설 ID (외래키)

    @NotNull(message = "요일은 필수입니다")
    private DayOfWeek dayOfWeek;  // 요일 (MONDAY, TUESDAY 등)

    @NotNull(message = "시작 시간은 필수입니다")
    @JsonFormat(pattern = "HH:mm")  // 24시간 형식으로 지정
    private LocalTime startTime;    // 강의 시작 시간

    @NotNull(message = "종료 시간은 필수입니다")
    @JsonFormat(pattern = "HH:mm")  // 24시간 형식으로 지정
    private LocalTime endTime;      // 강의 종료 시간

    @NotBlank(message = "강의실은 필수입니다")
    private String classroom;       // 강의실 위치

    // 시간 차이 계산 메서드
    public int getDurationHours() {
        return (int) ChronoUnit.HOURS.between(startTime, endTime);
    }

    // 시간 유효성 검사
    @AssertTrue(message = "종료 시간은 시작 시간보다 늦어야 합니다")
    private boolean isValidTimeRange() {
        if (startTime == null || endTime == null) {
            return true;  // @NotNull에서 처리됨
        }
        return endTime.isAfter(startTime);
    }
}