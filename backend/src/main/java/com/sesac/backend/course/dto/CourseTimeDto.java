package com.sesac.backend.course.dto;

import com.sesac.backend.course.entity.CourseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseTimeDto {
    private String timeId;
    private String openingId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String classroom;

    public static CourseTimeDto from(CourseTime courseTime) {
        return new CourseTimeDto(
                courseTime.getTimeId(),
                courseTime.getOpeningId(),
                courseTime.getDayOfWeek(),
                courseTime.getStartTime(),
                courseTime.getEndTime(),
                courseTime.getClassroom()
        );
    }

    public CourseTime toEntity() {
        CourseTime courseTime = new CourseTime();
        courseTime.setTimeId(this.timeId);
        courseTime.setOpeningId(this.openingId);
        courseTime.setDayOfWeek(this.dayOfWeek);
        courseTime.setStartTime(this.startTime);
        courseTime.setEndTime(this.endTime);
        courseTime.setClassroom(this.classroom);
        return courseTime;
    }
}