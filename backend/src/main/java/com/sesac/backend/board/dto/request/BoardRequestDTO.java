package com.sesac.backend.board.dto.request;

import com.sesac.backend.board.constant.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequestDTO {
    @NotBlank(message = "게시판 이름은 필수입니다")
    private String boardName;        // 게시판 이름

    @NotNull(message = "게시판 유형은 필수입니다")
    private BoardType boardType;     // 게시판 유형

    private boolean allowAnonymous;  // 익명 허용 여부
    private boolean allowComments;   // 댓글 허용 여부
    private boolean allowEdit;       // 수정 허용 여부
    private boolean allowDelete;     // 삭제 허용 여부
}