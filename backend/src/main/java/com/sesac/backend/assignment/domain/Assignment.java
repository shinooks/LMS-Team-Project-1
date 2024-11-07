package com.sesac.backend.assignment.domain;

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
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Assignment {

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
    private String title;
    private String description;
    private LocalDateTime deadline;
}
