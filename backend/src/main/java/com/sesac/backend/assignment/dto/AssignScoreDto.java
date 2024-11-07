package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.AssignScore.Visibility;
import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.entity.Student;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 과제 점수 dto
 * AssignScore 데이터 전달 객체
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignScoreDto {

    /**
     * assignScoreId:   PK
     * assignment:      점수를 생성한 과제
     * student:         점수를 받은 학생
     * score:           점수
     * comment:         교수 코멘트
     * visibility:      조회가능여부
     */
    private UUID assignScoreId;
    private Assignment assignment;
    private Student student;
    private int score;
    private String comment;
    private Visibility visibility;
}
