package com.sesac.backend.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EnrollmentResultDto {

    private UUID openingId;
    private String courseCode;
    private String courseName;
    private String message;
    private Integer maxStudents;
    private Integer currentStudents;
    private boolean success;

}
