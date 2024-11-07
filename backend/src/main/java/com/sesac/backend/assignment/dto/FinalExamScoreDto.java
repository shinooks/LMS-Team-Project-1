package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.FinalExam;
import com.sesac.backend.assignment.domain.FinalExamScore.Visibility;
import com.sesac.backend.entity.Student;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinalExamScoreDto {
    private UUID finalExamScoreId;
    private FinalExam finalExam;
    private Student student;
    private int score;
    private Visibility visibility;
}
