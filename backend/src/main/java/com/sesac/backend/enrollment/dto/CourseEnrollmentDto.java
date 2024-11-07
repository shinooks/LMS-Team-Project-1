package com.sesac.backend.enrollment.dto;

import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
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

    private Student student; // 일단 등록할 학생 이름

    private CourseOpening courseOpening; // 최대인원, 현재인원, courseTimes

    private String courseName; // unique 제약 설정

    private LocalDateTime enrollmentDate = LocalDateTime.now();
}
