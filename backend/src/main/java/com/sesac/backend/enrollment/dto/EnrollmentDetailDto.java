package com.sesac.backend.enrollment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sesac.backend.course.constant.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EnrollmentDetailDto {
    @JsonProperty("enrollmentId")
    private UUID enrollmentId;

    @JsonProperty("courseCode")       // JSON에서 "courseCode"로 사용
    private String courseCode;       // 강의 코드

    @JsonProperty("courseName")       // JSON에서 "courseName"으로 사용
    private String courseName;       // 강의명

    @JsonProperty("credits")
    private Integer credits;

    @JsonProperty("dayOfWeek")       // JSON에서 "dayOfWeek"으로 사용
    private DayOfWeek dayOfWeek;     // 요일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    @JsonProperty("currentStudents")
    private Integer currentStudents;

    @JsonProperty("maxStudents")
    private Integer maxStudents;

}