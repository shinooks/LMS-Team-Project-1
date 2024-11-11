package com.sesac.backend.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class GetAllCourseInfoDto {
    private String courseCode;
    private String courseName;
    private Integer credits;
    private Integer maxStudents;
    private Integer currentStudents;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
