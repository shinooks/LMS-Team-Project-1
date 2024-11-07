package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.FinalExam;
import com.sesac.backend.assignment.domain.FinalExamScore.Visibility;
import com.sesac.backend.entity.Student;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 기말고사 점수 dto
 * FinalExamScore 데이터 전달 객체
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinalExamScoreDto {

    /**
     * finalExamScoreId:    PK
     * finalExam:           점수를 생성한 기말고사
     * student:             점수를 받은 학생
     * score:               점수
     * visibility:          조회 가능 여부
     */
    private UUID finalExamScoreId;
    private FinalExam finalExam;
    private Student student;
    private int score;
    private Visibility visibility;
}
