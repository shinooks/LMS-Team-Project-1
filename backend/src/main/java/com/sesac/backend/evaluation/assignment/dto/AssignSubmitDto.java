package com.sesac.backend.evaluation.assignment.dto;

import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 과제 제출 dto
 * AssignSubmit 데이터 전달 객체
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignSubmitDto {

    /**
     * assignmentSubmitId:  PK
     * assignment:          제출 받을 과제
     * student:             제출 생성한 학생
     * answer:              제출한 답안
     * fileName:            제출 파일명
     */
    private UUID assignSubmitId;
    private UUID assignId;
    private UUID studentId;
    private String answer;
    private String fileName;
}
