package com.sesac.backend.board.dto.request;

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
public class PostLikeRequestDTO {
    @NotNull(message = "게시글 ID는 필수입니다")
    private UUID postId;            // 게시글 ID
}