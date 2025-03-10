package com.sesac.backend.evaluation.assignment.dto.request;

import java.time.LocalDate;
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
public class AssignCreationRequest {

    private UUID openingId;
    private UUID studentId;
    private String title;
    private String description;
    private LocalDate openAt;
    private LocalDate deadline;
}
