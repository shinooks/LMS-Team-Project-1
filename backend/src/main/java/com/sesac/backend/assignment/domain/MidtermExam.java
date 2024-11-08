package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Course;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MidtermExam {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID midtermExamId;
    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
