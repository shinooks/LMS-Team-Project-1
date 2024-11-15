package com.sesac.backend.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GradeAppeal {
    @Id
    @GeneratedValue
    private UUID appealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeId")
    private Grade grade;

    @Column(nullable = false)
    private String content;  // 이의신청 내용

    @Enumerated(EnumType.STRING)
    private AppealStatus status = AppealStatus.PENDING;  // 처리상태

    private LocalDateTime createdAt;  // 신청일시

    private int requestedAssignScore;
    private int requestedMidtermScore;
    private int requestedFinalScore;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

