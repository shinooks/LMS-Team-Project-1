package com.sesac.backend.assignment.dto;

import com.sesac.backend.course.domain.Course;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {
    private UUID assignId;
    private Course course;
    private String title;
    private String description;
}
