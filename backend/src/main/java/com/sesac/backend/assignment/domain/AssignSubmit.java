package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
