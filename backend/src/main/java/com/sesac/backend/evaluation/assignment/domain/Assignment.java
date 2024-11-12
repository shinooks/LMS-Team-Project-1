package com.sesac.backend.evaluation.assignment.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.enums.Visibility;
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
@Builder
@ToString(exclude = {"courseOpening", "student"})
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
    private CourseOpening courseOpening;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String description;
    private UUID scanId;
    @Column(columnDefinition = "bytea")
    private byte[] file;
    private String fileName;
    private String copyleaksResult;
    private LocalDateTime openAt;
    private LocalDateTime deadline;
}
