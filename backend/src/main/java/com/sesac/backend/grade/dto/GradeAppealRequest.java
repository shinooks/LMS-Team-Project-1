package com.sesac.backend.grade.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GradeAppealRequest {
    private UUID gradeId;
    private String content;
    private int requestedAssignScore;
    private int requestedMidtermScore;
    private int requestedFinalScore;
}
