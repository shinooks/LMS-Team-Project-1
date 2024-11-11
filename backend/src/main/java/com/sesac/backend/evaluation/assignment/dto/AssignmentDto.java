package com.sesac.backend.evaluation.assignment.dto;

import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 과제 dto
 * Assignment 데이터 전달 객체
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AssignmentDto {

    /**
     * assignId:    PK
     * course:      과제 생성한 강의
     * title:       제목
     * description: 문제
     * deadline:    제출기한
     */
    private UUID assignId;
    private UUID courseId;
    private String title;
    @Lob
    private String description;
    private LocalDateTime deadline;
}
