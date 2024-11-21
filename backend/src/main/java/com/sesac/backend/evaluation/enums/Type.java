package com.sesac.backend.evaluation.enums;

import lombok.Getter;

@Getter
public enum Type {
    MIDTERM("중간고사"), FINAL("기말고사");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public static Type getType(String value) {
        for (Type type : Type.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
