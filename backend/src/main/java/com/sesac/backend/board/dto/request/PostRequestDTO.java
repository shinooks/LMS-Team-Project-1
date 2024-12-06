package com.sesac.backend.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {
    @NotNull(message = "게시판 ID는 필수입니다")
    private UUID boardId;           // 게시판 ID

    @NotBlank(message = "제목은 필수입니다")
    private String title;           // 제목

    @NotBlank(message = "내용은 필수입니다")
    private String content;         // 내용

    private boolean anonymous;    // 익명 여부
}