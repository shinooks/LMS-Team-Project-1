package com.sesac.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.dto.CourseTimeDto;
import com.sesac.backend.entity.CourseOpening;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "course_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseTime {  // 단순 시간 정보라 상속 불필요
    @Id
    @GeneratedValue
    private UUID timeId;    // 시간ID (고유 식별자)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening;  // 해당 강의 개설 정보

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;  // 요일 (월,화,수,목,금,토,일)

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")  // 24시간 형식으로 시간 표시
    private LocalTime startTime;    // 강의 시작 시간

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")  // 24시간 형식으로 시간 표시
    private LocalTime endTime;      // 강의 종료 시간

    @Column(nullable = false)
    private String classroom;      // 강의실 위치

    public void updateFromDto(CourseTimeDto dto) {
        this.dayOfWeek = dto.getDayOfWeek();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.classroom = dto.getClassroom();
    }
}