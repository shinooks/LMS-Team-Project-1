package com.sesac.backend.board.dto.request;

import com.sesac.backend.board.constant.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "게시글 검색 요청 DTO")
public class PostSearchRequestDto {
    @Schema(description = "검색 키워드", defaultValue = "")
    private String keyword = "";

    @Schema(description = "검색 유형 (title/content)", defaultValue = "title")
    private String searchType = "title";

    @Schema(description = "게시판 유형")
    private BoardType boardType;

    @Schema(description = "작성자 ID")
    private UUID authorId;

    @Schema(description = "정렬 기준", defaultValue = "createdAt")
    private String sortBy = "createdAt";

    @Schema(description = "정렬 방향 (ASC/DESC)", defaultValue = "DESC")
    private String sortDirection = "DESC";
}