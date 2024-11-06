package com.sesac.backend.enrollment.dto;

import com.sesac.backend.enrollment.domain.tempClasses.Classes;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassEnrollmentDto {

    private long enrollmentId;
    private String studentId; // 학생 ID
    private ClassesDto classes; // ClassesDto 객체
    private String className;

}
