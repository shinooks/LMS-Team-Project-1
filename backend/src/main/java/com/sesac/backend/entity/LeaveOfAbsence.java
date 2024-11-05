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
public class LeaveOfAbsence {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID leaveId=UUID.randomUUID(); // 휴학ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student; // 학생ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType; // 휴학유형

    @Column(nullable = false)
    private LocalDate startDate; // 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 종료일

    private String reason; // 사유

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.APPLIED; // 상태

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt = LocalDateTime.now(); // 신청일시

    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일시

    public enum LeaveType {
        GENERAL_LEAVE, MILITARY_LEAVE, MEDICAL_LEAVE, PARENTAL_LEAVE, STARTUP_LEAVE
    }

    public enum LeaveStatus {
        APPLIED, APPROVED, REJECTED
    }
}
