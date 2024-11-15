package com.sesac.backend.evaluation.assignment.dto.request;

import java.util.UUID;
import lombok.*;

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
