package com.sesac.backend.evaluation.exam.dto.response;

import com.sesac.backend.evaluation.enums.Type;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExamCreationResponse {

    private UUID openingId;
    private Type type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
