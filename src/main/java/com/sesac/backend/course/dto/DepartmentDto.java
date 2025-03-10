package com.sesac.backend.course.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private UUID departmentId;

    @NotBlank(message = "학과명은 필수입니다")
    private String departmentName;  // 예: "컴퓨터공학과"
}