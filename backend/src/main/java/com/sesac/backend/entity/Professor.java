package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor {

    @Id
    @GeneratedValue
    private UUID professorId; // 교수ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserAuthentication user; // 사용자ID

    @Column(nullable = false, unique = true)
    private String professorNumber; // 교번

    @Column(nullable = false)
    private String name; // 교수 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId", nullable = false)
    private Department department; // 학과ID

    @OneToMany(mappedBy = "professor")
    private List<CourseOpening> courseOpenings;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일시

    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일시
}
