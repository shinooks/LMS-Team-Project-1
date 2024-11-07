package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Course;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

/**
 * @author dongjin
 * 중간고사 도메인
 * MidtermExam 테이블 컬럼 정의
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MidtermExam {

    /**
     * midtermExamId:       PK
     * course:              중간고사 생성한 강의
     * startTime:           시작시간
     * endTime:             종료시간
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID midtermExamId;
    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
