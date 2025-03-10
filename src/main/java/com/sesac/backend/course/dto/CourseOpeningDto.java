package com.sesac.backend.course.dto;

import com.sesac.backend.course.constant.CourseStatus;
import com.sesac.backend.entity.Professor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseOpeningDto {
    private UUID openingId;

    @NotNull(message = "강의 정보는 필수입니다")
    private UUID courseId;  // Course 엔티티의 ID

    @NotBlank(message = "교수 정보는 필수입니다")
    private ProfessorDto professor;

    @NotBlank(message = "학기는 필수입니다")
    private String semester;

    @NotNull(message = "연도는 필수입니다")
    private Integer year;

    @NotNull(message = "최대 수강인원은 필수입니다")
    private Integer maxStudents;

    private Integer currentStudents;

    @NotNull(message = "강의 상태는 필수입니다")
    private CourseStatus status;

    private List<CourseTimeDto> courseTimes; // 여러 강의 시간
    private SyllabusDto syllabus; // 하나의 강의 계획서
}
