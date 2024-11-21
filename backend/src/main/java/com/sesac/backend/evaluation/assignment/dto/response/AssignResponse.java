package com.sesac.backend.evaluation.assignment.dto.response;

import com.sesac.backend.evaluation.enums.Priority;
import com.sesac.backend.evaluation.enums.Status;
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
public class AssignResponse {

    private UUID assignId;
    private String title;
    private String courseName;
    private LocalDate deadline;
    private Status status;
    private Priority priority;
}
