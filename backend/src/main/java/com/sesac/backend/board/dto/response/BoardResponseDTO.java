package com.sesac.backend.board.dto.response;

import com.sesac.backend.board.constant.BoardType;
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
public class BoardResponseDTO {
    private UUID boardId;           // 게시판 ID
    private String boardName;       // 게시판 이름
    private BoardType boardType;    // 게시판 유형
    private boolean allowAnonymous; // 익명 허용 여부
    private boolean allowComments;  // 댓글 허용 여부
    private boolean allowEdit;      // 수정 허용 여부
    private boolean allowDelete;    // 삭제 허용 여부
    private LocalDateTime createdAt;// 생성일시
    private String createdBy;       // 생성자
}