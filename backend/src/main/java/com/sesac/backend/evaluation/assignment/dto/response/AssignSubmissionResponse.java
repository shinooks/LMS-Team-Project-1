package com.sesac.backend.evaluation.assignment.dto.response;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignSubmissionResponse {

    private UUID assignId;
    private UUID studentId;
    private UUID openingId;
}
