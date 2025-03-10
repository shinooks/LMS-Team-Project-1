package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSchedule {

    @Id
    @GeneratedValue
    private UUID scheduleId; // 시간ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course; // 개설ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek; // 요일

    @Column(nullable = false)
    private LocalTime startTime; // 시작시간

    @Column(nullable = false)
    private LocalTime endTime; // 종료시간

    @Column(nullable = false)
    private String classroom; // 강의실 위치

    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
