package com.sesac.backend.course.constant;

public enum CourseStatus {
    PENDING("개설예정"),
    OPENED("개설"),
    CANCELLED("폐강"),
    CLOSED("종료");

    private final String description;

    CourseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
