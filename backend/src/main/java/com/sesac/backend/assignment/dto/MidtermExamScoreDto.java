package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.MidtermExam;
import com.sesac.backend.assignment.domain.MidtermExamScore.Visibility;
import com.sesac.backend.entity.Student;
import java.util.UUID;
import lombok.*;


/**
 * @author dongjin
 * 중간고사 점수 dto
 * MidtermExamScore 데이터 전달 객체
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MidtermExamScoreDto {

    /**
     * midtermExamScoreId:      PK
     * midtermExam:             점수를 생성한 중간고사
     * student:                 점수를 받은 학생
     * score:                   점수
     * visibility:              조회 가능 여부
     */
    private UUID midtermExamScoreId;
    private MidtermExam midtermExam;
    private Student student;
    private int score;
    private Visibility visibility;
}
