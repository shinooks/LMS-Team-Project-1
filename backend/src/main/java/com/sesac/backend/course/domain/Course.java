package com.sesac.backend.course.domain;

import com.sesac.backend.entity.Department;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue
    private UUID courseId; // 강의ID

    @Column(nullable = false, unique = true)
    private String courseCode; // 강의코드

    @Column(nullable = false)
    private String courseName; // 강의명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId", nullable = false)
    private Department department; // 학과ID

    @Column(nullable = false)
    private int credits; // 학점

    private String description; // 설명
}
