package com.sesac.backend.evaluation.assignment.dto;

import jakarta.persistence.Lob;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignSubmissionRequest {
    private UUID studentId;
    private UUID openingId;
    @Lob
    private byte[] file;
}
