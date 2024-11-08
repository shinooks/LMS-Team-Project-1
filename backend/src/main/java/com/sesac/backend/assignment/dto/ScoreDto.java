package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.assignment.domain.FinalExam;
import com.sesac.backend.assignment.domain.MidtermExam;
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
    private Assignment assignment;
    private MidtermExam midtermExam;
    private FinalExam finalExam;
    private Student student;
    private int assignScore;
    private int midtermExamScore;
    private int finalExamScore;
    private Visibility visibility;
}
