package com.sesac.backend.grade.domain;

import com.sesac.backend.assignment.domain.Assignment_Dome;
import com.sesac.backend.grade.dto.requset.GradeUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;


/**
 * 성적 정보를 관리하는 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment_Dome assignmentDome;

    /**
     * Assignment_Dome에서 총점을 가져오는 메서드
     * @return 계산된 총점
     */
    public Double getScore() {
        return assignmentDome.getTotalScore();
    }

    /**
     * 개별 점수 조회 메서드들
     */
    public Double getAssignmentScore() {
        return assignmentDome.getAssignmentScore();
    }

    public Double getMidtermScore() {
        return assignmentDome.getMidtermScore();
    }

    public Double getFinalScore() {
        return assignmentDome.getFinalScore();
    }


    public void updateScores(GradeUpdateRequestDto requestDto) {
        if (requestDto.getAssignmentScore() != null) {
            assignmentDome.setAssignmentScore(requestDto.getAssignmentScore());
        }
        if (requestDto.getMidtermScore() != null) {
            assignmentDome.setMidtermScore(requestDto.getMidtermScore());
        }
        if (requestDto.getFinalScore() != null) {
            assignmentDome.setFinalScore(requestDto.getFinalScore());
        }

        assignmentDome.calculateTotalScore();
    }


}