package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.entity.Student;
import java.time.LocalDateTime;
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
@AllArgsConstructor
@NoArgsConstructor
public class AssignSubmitDto {

    /**
     * assignmentSubmitId:  PK
     * assignment:          제출 받을 과제
     * student:             제출 생성한 학생
     * answer:              제출한 답안
     * submitAt:            제출 일시
     * fileName:            제출 파일명
     */
    private UUID assignSubmitId;
    private Assignment assignment;
    private Student student;
    private String answer;
    private LocalDateTime submitAt;
    private String fileName;
}
