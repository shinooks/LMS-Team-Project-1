package com.sesac.backend.assignment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment_Dome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Double assignmentScore;
    private Double midtermScore;
    private Double finalScore;
    private Double totalScore;

    public void setMidtermScore(Double score) {
        this.midtermScore = score;
    }

    public void setFinalScore(Double score) {
        this.finalScore = score;
    }

    public void calculateTotalScore() {
        this.totalScore = (assignmentScore * 0.3) +
                (midtermScore * 0.3) +
                (finalScore * 0.4);
    }
}