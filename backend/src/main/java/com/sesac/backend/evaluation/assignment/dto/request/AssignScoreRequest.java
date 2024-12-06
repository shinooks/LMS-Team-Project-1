package com.sesac.backend.evaluation.assignment.dto.request;

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
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignScoreRequest {

    private UUID assignId;
    private UUID openingId;
    private UUID studentId;
    private int score;
}
