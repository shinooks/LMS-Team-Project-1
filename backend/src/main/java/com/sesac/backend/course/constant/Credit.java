package com.sesac.backend.course.constant;

import lombok.Getter;

@Getter
public enum Credit {
    ONE(1, "1학점"),
    TWO(2, "2학점"),
    THREE(3, "3학점");

    private final int value;
    private final String description;

    Credit(int value, String description) {
        this.value = value;
        this.description = description;
    }


}