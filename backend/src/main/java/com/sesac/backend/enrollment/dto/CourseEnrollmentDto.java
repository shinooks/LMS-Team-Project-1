package com.sesac.backend.enrollment.dto;

import com.sesac.backend.entity.Student;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseEnrollmentDto {

    private UUID enrollmentId;
    private Student student; // 학생 ID
    private CourseDto course; // ClassesDto 객체
    private String courseName;
    private LocalDateTime enrollmentDate;
}
