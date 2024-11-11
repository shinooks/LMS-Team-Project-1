package com.sesac.backend.evaluation.assignment.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.Course;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

/**
 * @author dongjin
 * 과제 도메인
 * Assignment 테이블 컬럼 정의
 */
@Getter
@Builder
@ToString(exclude = {"course"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Assignment extends BaseEntity {

    /**
     * assignId:    PK
     * course:      과제 생성한 강의
     * title:       제목
     * description: 문제
     * deadline:    제출기한
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assignId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String description;
    private LocalDateTime deadline;

    public void setAssign(Course course, String title, String description, LocalDateTime deadline) {
        this.course = course;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }
}
