package com.sesac.backend.grade.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Entity;
import lombok.Table;
import lombok.Id;
import lombok.GeneratedValue;
import lombok.GenerationType;
import lombok.JoinColumn;
import lombok.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment_Dome assignment_dome;

    // 성적 수정 메서드
    public void updateScore(Double newScore) {
        assignment_dome.updateScore(newScore); // Assignment_Dome의 점수 업데이트
    }
} ㅣ