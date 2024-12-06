package com.sesac.backend.evaluation.exam.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExamProblemReadDto {

    private UUID problemId;
    private Integer number;
    private String question;
    private int difficulty;
    private List<String> options;
}
