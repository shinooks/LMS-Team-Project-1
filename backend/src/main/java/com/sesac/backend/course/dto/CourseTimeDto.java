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
    private UUID timeId;

    @NotNull(message = "요일은 필수입니다")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "시작 시간은 필수입니다")
    @JsonFormat(pattern = "HH:mm")  // 24시간 형식으로 지정
    private LocalTime startTime;   // 14:00 오후 2시

    @NotNull(message = "종료 시간은 필수입니다")
    @JsonFormat(pattern = "HH:mm")  // 24시간 형식으로 지정
    private LocalTime endTime;     // 15:00 오후 3시

    @NotBlank(message = "강의실은 필수입니다")
    private String classroom;

    public int getDurationHours() { // 강의 시작 시간과 종료 시간 사이의 시간 차이를 계산
        return (int) ChronoUnit.HOURS.between(startTime, endTime); // 자바의 시간 계산 메서드
    }

    @AssertTrue(message = "종료 시간은 시작 시간보다 늦어야 합니다")
    private boolean isValidTimeRange() {
        if (startTime == null || endTime == null) {
            return true;  // @NotNull에서 처리됨
        }
        return endTime.isAfter(startTime);
    }
}