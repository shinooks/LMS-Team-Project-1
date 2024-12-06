package com.sesac.backend.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLikeResponseDTO {
    private UUID likeId;            // 좋아요 ID
    private UUID postId;            // 게시글 ID
    private String userName;        // 사용자 이름
    private LocalDateTime createdAt;// 좋아요 일시
}