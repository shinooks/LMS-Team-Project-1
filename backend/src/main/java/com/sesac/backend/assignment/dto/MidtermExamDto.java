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
public class MidtermExamDto {
    private UUID midtermExamId;
    private Course course;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
