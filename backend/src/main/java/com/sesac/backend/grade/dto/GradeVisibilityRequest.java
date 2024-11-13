package com.sesac.backend.grade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class GradeVisibilityRequest {
    private UUID openingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}
