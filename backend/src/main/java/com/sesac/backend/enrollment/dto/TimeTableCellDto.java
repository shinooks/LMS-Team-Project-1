package com.sesac.backend.enrollment.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TimeTableCellDto {

    private UUID enrollmentId;
    private UUID openingId;
    private String courseCode;
    private String courseName;

}

