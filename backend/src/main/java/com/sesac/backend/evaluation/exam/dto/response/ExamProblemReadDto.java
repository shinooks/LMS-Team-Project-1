package com.sesac.backend.evaluation.exam.dto.response;

import com.sesac.backend.evaluation.enums.Difficulty;
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
    private Difficulty difficulty;
    private String question;
    private List<String> choices;
}
