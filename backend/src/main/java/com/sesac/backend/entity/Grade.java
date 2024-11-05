package com.sesac.backend.entity;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignScoreId", nullable = false)
    private AssignScore assignScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;    // Course 엔티티 추가



    // AssignScore에서 값을 가져오는 Getter 메서드들
    public int getAssignmentScore() {
        return assignScore.getAssignmentScore();
    }

    public int getMidScore() {
        return assignScore.getMidScore();
    }

    public int getFinalScore() {
        return assignScore.getFinalScore();
    }

    public String getTerm() {
        return course.getTerm();
    }

    public String getCourseName() {
        return course.getCourseName();
    }

    public String getCourseCode() {
        return course.getCourseCode();
    }



    // 총점 계산
    public int getTotalScore() {
        return getAssignmentScore() + getMidScore() + getFinalScore();
    }
}
