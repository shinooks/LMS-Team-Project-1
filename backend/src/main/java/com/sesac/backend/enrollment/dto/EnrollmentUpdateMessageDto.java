package com.sesac.backend.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentUpdateMessageDto {

    private UUID openingId;
    private UUID studentId;
    private Integer currentEnrollment;
}
