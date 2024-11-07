package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author dongjin
 * 과제 제출 도메인
 * AssignSubmit 테이블 컬럼 정의
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AssignSubmit {

    /**
     * assignmentSubmitId:  PK
     * assignment:          제출 받을 과제
     * student:             제출 생성한 학생
     * answer:              제출한 답안
     * submitAt:            제출 일시
     * fileName:            제출 파일명
     */
    @Id
    @GeneratedValue
    private UUID assignSubmitId;
    @ManyToOne
    @JoinColumn(name = "assignId")
    private Assignment assignment; // 과제
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private Student student; // 학생
    private String answer; // 제출답안
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime submitAt; // 제출일시
    private String fileName;
}
