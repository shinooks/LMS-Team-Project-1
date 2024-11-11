package com.sesac.backend.assignment.dto;

import com.sesac.backend.entity.Course;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AssignmentDto {
    private UUID assignId;
    private UUID courseId;
    private String title;
    private String description;
    private LocalDateTime deadline;
}
