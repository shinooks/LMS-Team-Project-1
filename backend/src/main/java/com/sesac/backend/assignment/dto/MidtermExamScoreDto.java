package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.MidtermExam;
import com.sesac.backend.assignment.domain.MidtermExamScore.Visibility;
import com.sesac.backend.entity.Student;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MidtermExamScoreDto {
    private UUID midtermExamScoreId;
    private MidtermExam midtermExam;
    private Student student;
    private int score;
    private Visibility visibility;
}
