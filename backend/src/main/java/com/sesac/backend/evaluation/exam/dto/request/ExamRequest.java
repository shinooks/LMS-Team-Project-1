package com.sesac.backend.evaluation.exam.dto.request;

import com.sesac.backend.evaluation.enums.Type;
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
public class ExamRequest {

    private UUID openingId;
    private UUID studentId;
    private Type type;
}
