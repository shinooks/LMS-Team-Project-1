package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Course;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assignId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course; // 출제 강의
    private String title; // 제목
    private String description; // 문제
    private LocalDateTime deadline; // 제출기한
}
