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
public class FinalExam {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalExamId;
    @OneToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
