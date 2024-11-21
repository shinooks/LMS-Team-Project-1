package com.sesac.backend.evaluation.assignment.dto.response;

import java.time.LocalDateTime;
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
public class AssignUpdateResponse {

    private UUID assignId;
    private UUID openingId;
    private UUID studentId;
    private String title;
    private String description;
    private LocalDateTime openAt;
    private LocalDateTime deadline;
}
