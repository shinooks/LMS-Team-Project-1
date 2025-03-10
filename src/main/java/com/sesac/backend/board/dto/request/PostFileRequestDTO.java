package com.sesac.backend.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFileRequestDTO {
    @NotNull(message = "게시글 ID는 필수입니다")
    private UUID postId;                // 게시글 ID


    @NotBlank(message = "파일명은 필수입니다")
    private String originalName;        // 원본 파일명


    private String storedName;          // 저장된 파일명

    private String filePath;            // 파일 경로

    @Positive(message = "파일 크기는 0보다 커야 합니다")
    private Long fileSize;              // 파일 크기

    @NotBlank(message = "파일 타입은 필수입니다")
    private String fileType;            // 파일 MIME 타입

    private String createdBy;
}