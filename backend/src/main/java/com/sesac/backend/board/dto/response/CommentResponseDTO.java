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
public class CommentResponseDTO {
    private UUID commentId;         // 댓글 ID
    private UUID postId;            // 게시글 ID
    private String content;         // 댓글 내용
    private String authorName;      // 작성자 이름
    private boolean isAnonymous;    // 익명 여부
    private LocalDateTime createdAt;// 작성일시
    private String createdBy;       // 작성자
}