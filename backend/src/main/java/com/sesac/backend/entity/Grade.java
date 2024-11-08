package com.sesac.backend.entity;

import com.sesac.backend.assignment.domain.AssignScore;
import com.sesac.backend.assignment.domain.FinalExamScore;
import com.sesac.backend.assignment.domain.MidtermExamScore;
import com.sesac.backend.assignment.domain.Score;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
    private UUID gradeId;

//    // 학생 정보와의 다대일 관계
//    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 최적화
//    @JoinColumn(name = "studentId", nullable = false)
//    private Student student;


//    // 과제/시험 점수와의 다대일 관계
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "assignScoreId", nullable = false)
//    private AssignScore assignScore;
//
//    // 시험 점수와의 다대일 관계
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "midtermExamScoreId", nullable = false)
//    private MidtermExamScore midtermExamScore;
//
//    // 시험 점수와의 다대일 관계
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "finalExamScoreId", nullable = false)
//    private FinalExamScore finalExamScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scoreId", nullable = false)
    private Score score;



    // 과목 정보와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    // 강의 개설 정보와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening;



//    // AssignScore 엔티티에서 각 점수를 조회하는 편의 메서드들
//    public int getAssignScoreInteger() {
//        return assignScore.getScore();
//    }

    public int getAssignScore() {
        return score.getAssignScore();
    }

    public int getMidtermScore() {
        return score.getMidtermExamScore();
    }

    public int getFinalScore() {
        return score.getFinalExamScore();
    }

//    public int getMidtermExamScore() {
//        return midtermExamScore.getScore();
//    }
//
//    public int getFinalExamScore() {
//        return finalExamScore.getScore();
//    }


    /**
     * 과제, 중간고사, 기말고사 점수의 합계를 계산
     * 과제: 20%, 중간고사: 40%, 기말고사: 40%의 가중치를 적용
     * @return 총점
     */
    public int getTotalScore() {
        int assignmentScore = getAssignScore(); // 과제 점수
        int midtermScore = getMidtermScore(); // 중간고사 점수
        int finalExamScore = getFinalScore(); // 기말고사 점수

        // 가중치 적용하여 총점 계산 후 반올림
        return (int) Math.round(assignmentScore * 0.2 + midtermScore * 0.4 + finalExamScore * 0.4);
    }



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



}