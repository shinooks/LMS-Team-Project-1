package com.sesac.backend.assignment.dto;

import com.sesac.backend.entity.AssignScore.Visibility;
import com.sesac.backend.entity.Assignment;
import com.sesac.backend.entity.Student;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignScoreDto {

    private UUID assignScoreId;
    private Assignment assignment;
    private Student student;
    private int score;
    private String comment;
    private Visibility visibility;
}
