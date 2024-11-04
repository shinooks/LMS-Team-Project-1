package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue
    private UUID studentId; // 학생ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserAuthentication user; // 사용자ID

    @Column(nullable = false, unique = true)
    private String studentNumber; // 학번

    @Column(nullable = false)
    private String name; // 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId", nullable = false)
    private Department department; // 학과ID

    @Column(nullable = false)
    private int enrollmentYear; // 입학년도

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 학적상태

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일시

    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일시

    public enum Status {
        ENROLLED, ON_LEAVE, GRADUATED, DELAYED_GRADUATION, WITHDRAWN, DROPOUT
    }
}
