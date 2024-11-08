package com.sesac.backend.entity;

/*
* 2024/11/08 - 정진욱
* feature/enrollment2 branch에서 사용하던 entity의 내용을
* entity 폴더의 enrollment entity로 변경
*/

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "opening_id"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID enrollmentId;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
    private Student student; // 일단 등록할 학생 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening;

    @Column(nullable = false)
    private String courseName; // unique 제약 설정

    @Column(nullable = false, updatable = false)
    private LocalDateTime enrollmentDate = LocalDateTime.now();

    // 생성자 또는 메서드에서 클래스의 className을 설정
    public Enrollment(CourseOpening courseOpening) {
        this.courseName = courseOpening.getCourse().getCourseName(); // Classes 엔터티의 className으로 설정
    }

    public Enrollment(Student student, CourseOpening openingCourseInfo, String courseName) {
        this.student = student;
        this.courseOpening = openingCourseInfo;
        this.courseName = courseName;
    }
}
