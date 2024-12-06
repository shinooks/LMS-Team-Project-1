package com.sesac.backend.enrollment.dto;

import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnrollmentDto {

    private UUID enrollmentId;

    private UUID studentId; // 일단 등록할 학생 이름

    private UUID openingId; // 최대인원, 현재인원, courseTimes

    private LocalDateTime enrollmentDate = LocalDateTime.now();
}
