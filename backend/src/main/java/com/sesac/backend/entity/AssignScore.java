package com.sesac.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignScore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assignScoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignId", nullable = false)
    private Assignment assignment;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    private int score;
}
