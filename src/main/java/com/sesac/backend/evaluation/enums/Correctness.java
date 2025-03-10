package com.sesac.backend.evaluation.enums;

import lombok.Getter;

@Getter
public enum Correctness {
    CORRECT(1), WRONG(0);

    private final int value;

    Correctness(int value) {
        this.value = value;
    }
}
