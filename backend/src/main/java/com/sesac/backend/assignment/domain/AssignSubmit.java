package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.BaseEntity;
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
@Builder
@ToString(exclude = {"assignment", "student"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class AssignSubmit extends BaseEntity {

    /**
     * assignmentSubmitId:  PK
     * assignment:          제출 받을 과제
     * student:             제출 생성한 학생
     * answer:              제출한 답안
     * fileName:            제출 파일명
     */
    @Id
    @GeneratedValue
    private UUID assignSubmitId;
    @ManyToOne
    @JoinColumn(name = "assignId", nullable = false)
    private Assignment assignment; // 과제
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student; // 학생
    @Lob
    @Column(nullable = false)
    private String answer; // 제출답안
    private String fileName;
}
