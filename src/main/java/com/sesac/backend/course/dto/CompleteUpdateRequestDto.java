package com.sesac.backend.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompleteUpdateRequestDto {
    private CourseOpeningDto courseOpening;
    private List<CourseTimeDto> courseTimes;
}
