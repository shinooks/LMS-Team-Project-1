package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicSchedule {

    @Id
    @GeneratedValue
    private UUID scheduleId; // 일정ID

    @Column(nullable = false)
    private String scheduleName; // 일정명

    @Column(nullable = false)
    private String scheduleType; // 일정유형

    @Column(nullable = false)
    private LocalDate startDate; // 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 종료일

    private String description; // 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffId", nullable = false)
    private Staff creator; // 작성자ID

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일시

    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일시
}
