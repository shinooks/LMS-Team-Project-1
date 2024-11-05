package com.sesac.backend.assignment.dto;

import com.sesac.backend.entity.AssignScore.Visibility;
import com.sesac.backend.user.dto.StudentDto;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignScoreDto {

    private UUID assignScoreId;
    private AssignmentDto assignmentDto;
    private StudentDto studentDto;
    private int score;
    private Visibility visibility;
}
