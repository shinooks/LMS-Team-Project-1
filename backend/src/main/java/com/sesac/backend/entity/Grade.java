package com.sesac.backend.entity;

import com.sesac.backend.assignment.domain.Score;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 성적 정보를 관리하는 엔티티
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // JPA 엔티티는 기본 생성자가 필요함
public class Grade {

    @Id
    @GeneratedValue// 자동 증가 ID
    private UUID gradeId;

    // 점수 일대일 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scoreId", nullable = false)
    private Score score;  // Score 엔티티 참조

    // 강의 개설 정보와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening; // CourseOpening 엔티티 참조


    @Column(nullable = false)
    private boolean visibility = false;  // 성적 공개 여부

    @Column
    private LocalDateTime visibilityStartDate;  // 공개 시작일

    @Column
    private LocalDateTime visibilityEndDate;    // 공개 종료일


    public int getAssignScore() {
        return score.getAssignScore();
    }

    public int getMidtermScore() {
        return score.getMidtermExamScore();
    }

    public int getFinalScore() {
        return score.getFinalExamScore();
    }



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
        return courseOpening.getCourse().getCourseName();
    }

    public String getCourseCode() {
        return courseOpening.getCourse().getCourseCode();
    }


    // 학생 정보를 조회하는 편의 메서드 추가
    public String getStudentNumber() {
        return score.getStudent().getStudentNumber();
    }

    public String getStudentName() {
        return score.getStudent().getName();
    }


}