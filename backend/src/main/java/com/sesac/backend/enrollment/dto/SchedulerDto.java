package com.sesac.backend.enrollment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchedulerDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
