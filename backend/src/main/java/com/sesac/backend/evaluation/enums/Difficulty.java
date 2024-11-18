package com.sesac.backend.evaluation.enums;

import lombok.Getter;

@Getter
public enum Difficulty {
    EASY(1), BASIC(2), NORMAL(3), HARD(4);

    private final int point;

    Difficulty(int point) {
        this.point = point;
    }
}