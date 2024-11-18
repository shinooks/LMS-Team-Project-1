package com.sesac.backend.course.dto;


import com.sesac.backend.course.constant.Credit;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private UUID courseId;

    @NotBlank(message = "강의 코드는 필수입니다")
    private String courseCode;

    @NotBlank(message = "강의명은 필수입니다")
    private String courseName;

    @NotNull(message = "학과 정보는 필수입니다")
    private UUID departmentId;  // Department 엔티티의 ID만 전달

    @NotNull(message = "학점은 필수입니다")
    private Credit credits;

    private String description;
}