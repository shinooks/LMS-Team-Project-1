package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.Score.Visibility;
import com.sesac.backend.entity.Student;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDto {

    private UUID scoreId;
    private UUID assignId;
    private UUID midtermExamId;
    private UUID finalExamId;
    private UUID studentId;
    private int assignScore;
    private int midtermExamScore;
    private int finalExamScore;
    private Visibility visibility;
}
