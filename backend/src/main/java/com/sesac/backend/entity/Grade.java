package com.sesac.backend.entity;

import com.sesac.backend.assignment.domain.AssignScore;
import com.sesac.backend.grade.dto.GradeDto;
import jakarta.persistence.*;
import lombok.*;

/**
 * 성적 정보를 관리하는 엔티티
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티는 기본 생성자가 필요함
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private Long gradeId;

    // 학생 정보와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 최적화
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;

    // 과제/시험 점수와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignScoreId", nullable = false)
    private AssignScore assignScore;

    // 과목 정보와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    // 강의 개설 정보와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening;



    // AssignScore 엔티티에서 각 점수를 조회하는 편의 메서드들
//    public int getAssignmentScore() {
//        return assignScore.getAssignmentScore();
//    }
//
//    public int getMidScore() {
//        return assignScore.getMidScore();
//    }
//
//    public int getFinalScore() {
//        return assignScore.getFinalScore();
//    }



    // CourseOpening 엔티티에서 학기 정보를 조회하는 편의 메서드
    public String getSemester() {
        return courseOpening.getSemester();
    }

    // Course 엔티티에서 과목 정보를 조회하는 편의 메서드들
    public String getCourseName() {
        return course.getCourseName();
    }

    public String getCourseCode() {
        return course.getCourseCode();
    }

    /**
     * 과제, 중간고사, 기말고사 점수의 합계를 계산
     * @return 총점
     */
//    public int getTotalScore() {
//        return getAssignmentScore() + getMidScore() + getFinalScore();
//    }


}