package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "opening_id"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID enrollmentId;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
    private Student student; // 일단 등록할 학생 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "openingId", nullable = false)
    private CourseOpening courseOpening;

    @Column(nullable = false, updatable = false)
    private LocalDateTime enrollmentDate = LocalDateTime.now();

}