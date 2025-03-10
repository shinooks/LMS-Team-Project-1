package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorPublication {

    @Id
    @GeneratedValue
    private UUID publicationId; // 논문ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professorId", nullable = false)
    private Professor professor; // 교수ID

    @Column(nullable = false)
    private String title; // 제목

    private String authorList; // 저자목록
    private LocalDate publicationDate; // 출판일
    private String journal; // 학술지
    private String doi; // DOI
}
