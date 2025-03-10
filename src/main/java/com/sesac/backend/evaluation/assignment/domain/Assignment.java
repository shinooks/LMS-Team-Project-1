package com.sesac.backend.evaluation.assignment.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.enums.Priority;
import com.sesac.backend.evaluation.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @Column(nullable = false)
    private String description;
    private UUID scanId;
    private String savedFileName;
    private String copyleaksResult;
    private LocalDate openAt;
    private LocalDate deadline;
    private Status status;
    private Priority priority;
}
