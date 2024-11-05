package com.sesac.backend.entity;

import com.sesac.backend.course.constant.CourseStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course_opening")
@Data
@NoArgsConstructor
public class CourseOpening extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID openingId;  // 개설ID (고유 식별자)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;   // 강의 정보 (Course 엔티티 참조)

    @Column(nullable = false)
    private String professorId;  // 교수ID (강의 담당 교수)

    @Column(nullable = false)
    private String semester;     // 학기 (예: "1", "2", "계절")

    @Column(nullable = false)
    private Integer year;        // 연도 (예: 2024)

    @Column(nullable = false)
    private Integer maxStudents; // 최대수강인원

    @Column(nullable = false)
    private Integer currentStudents = 0;  // 현재수강인원 (기본값 0)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status; // 강의 상태 (개설예정, 개설, 폐강, 종료)

    @OneToMany(mappedBy = "courseOpening", cascade = CascadeType.ALL)
    private List<CourseTime> courseTimes = new ArrayList<>();  // 강의 시간 목록

    @OneToOne(mappedBy = "courseOpening", cascade = CascadeType.ALL)
    private Syllabus syllabus;  // 강의 계획서

    // Builder 패턴을 사용하는 생성자
    @Builder
    public CourseOpening(Course course,         // 강의 정보
                         String professorId,      // 교수 ID
                         String semester,         // 학기
                         Integer year,            // 연도
                         Integer maxStudents,     // 최대수강인원
                         CourseStatus status) {   // 강의 상태
        this.course = course;
        this.professorId = professorId;
        this.semester = semester;
        this.year = year;
        this.maxStudents = maxStudents;
        this.status = status;
        this.currentStudents = 0;               // 현재수강인원 초기값
        this.courseTimes = new ArrayList<>();   // 강의 시간 목록 초기화
    }
}