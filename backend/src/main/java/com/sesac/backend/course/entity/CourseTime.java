package com.sesac.backend.course.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Table(name = "강의시간")
@Getter @Setter
@NoArgsConstructor
public class CourseTime {
    @Id
    @Column(name = "시간ID")
    private String timeId;

    @Column(name = "개설ID")
    private String openingId;

    @Column(name = "요일")
    private String dayOfWeek;

    @Column(name = "시작시간")
    private LocalTime startTime;

    @Column(name = "종료시간")
    private LocalTime endTime;

    @Column(name = "강의실")
    private String classroom;

    @ManyToOne
    @JoinColumn(name = "개설ID", insertable = false, updatable = false)
    private CourseOpening courseOpening;
}