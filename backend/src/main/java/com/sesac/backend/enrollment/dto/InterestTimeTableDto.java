package com.sesac.backend.enrollment.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterestTimeTableDto {

    private UUID enrollmentId;
    private UUID openingId;
    private String courseCode;
    private String courseName;
    private String day;
    private String startTime;
    private String endTime;
    private String classroom;

    // 생성자 추가
    public InterestTimeTableDto(UUID openingId, String courseCode, String courseName, String day, String startTime, String endTime, String classroom) {
        this.openingId = openingId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
    }
}