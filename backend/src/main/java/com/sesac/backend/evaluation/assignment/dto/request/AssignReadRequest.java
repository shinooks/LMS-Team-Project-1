package com.sesac.backend.evaluation.assignment.dto.request;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignReadRequest {

    private UUID studentId;
    private UUID courseId;
}
