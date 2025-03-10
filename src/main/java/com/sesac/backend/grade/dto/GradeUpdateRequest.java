package com.sesac.backend.grade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GradeUpdateRequest {

    private UUID gradeId;
    private int assignScore;
    private int midtermScore;
    private int finalScore;




}
