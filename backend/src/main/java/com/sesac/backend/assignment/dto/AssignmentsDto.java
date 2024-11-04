package com.sesac.backend.assignment.dto;

import com.sesac.backend.course.domain.CourseDummy;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentsDto {
    private long id;
    private CourseDummy courseDummy;
    private String title;
    private String description;
}
