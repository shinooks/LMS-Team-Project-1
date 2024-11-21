package com.sesac.backend.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDto {
    private UUID professorId;
    private String name;
    private String professorNumber;
    private UUID departmentId;
    private UUID userId;  // UserAuthentication 연결용
}