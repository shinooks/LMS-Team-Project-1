package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Assignment;
import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AssignSubmit {

    @Id
    @GeneratedValue
    private UUID assignSubmitId;
    @ManyToOne
    @JoinColumn(name = "assignId", nullable = false)
    private Assignment assignment; // 과제
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student; // 학생
    private String answer; // 제출답안
    @Column(nullable = false, updatable = false)
    private LocalDateTime submitAt = LocalDateTime.now(); // 제출일시
}
