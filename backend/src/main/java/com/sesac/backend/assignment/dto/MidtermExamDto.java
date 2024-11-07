package com.sesac.backend.assignment.dto;

import com.sesac.backend.entity.Course;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 중간고사 dto
 * MidtermExam 데이터 전달 객체
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MidtermExamDto {

    /**
     * midtermExamId:       PK
     * course:              중간고사 생성한 강의
     * startTime:           시작시간
     * endTime:             종료시간
     */
    private UUID midtermExamId;
    private Course course;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
