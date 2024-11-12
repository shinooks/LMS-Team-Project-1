package com.sesac.backend.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {
    private UUID postId;            // 게시글 ID
    private UUID boardId;           // 게시판 ID
    private String boardName;       // 게시판 이름
    private String title;           // 제목
    private String content;         // 내용
    private String authorName;      // 작성자 이름
    private boolean isAnonymous;    // 익명 여부
    private int viewCount;          // 조회수
    private int likeCount;          // 좋아요 수
    private LocalDateTime createdAt;// 작성일시
    private String createdBy;       // 작성자

    private List<PostLikeResponseDTO> likes;     // 좋아요 목록
    private List<CommentResponseDTO> comments;    // 댓글 목록
    private List<PostFileResponseDTO> files;// 첨부파일 목록 추가
}