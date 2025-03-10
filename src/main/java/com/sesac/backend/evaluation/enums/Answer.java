package com.sesac.backend.evaluation.enums;

public enum Answer {

    NOT_SELECTED(-2), ALL(-1), FIRST(0), SECOND(1), THIRD(2), FOURTH(3), FIFTH(4);

    private final int index;

    Answer(int index) {
        this.index = index;
    }
}
