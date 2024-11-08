package com.sesac.backend.assignment.dto;

import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.entity.Student;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AssignSubmitDto {

    private UUID assignSubmitId;
    private Assignment assignment;
    private Student student;
    private String answer;
    private LocalDateTime submitAt;
    private String fileName;
}
