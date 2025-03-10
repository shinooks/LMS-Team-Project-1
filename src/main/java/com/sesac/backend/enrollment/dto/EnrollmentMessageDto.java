package com.sesac.backend.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentMessageDto {

    private UUID studentId;
    private UUID openingId;
    private LocalDateTime requestTime;

}
