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
public class PostFileResponseDTO {
    private UUID fileId;               // 파일 ID
    private UUID postId;               // 게시글 ID
    private String originalName;       // 원본 파일명
    private String storedName;         // 저장된 파일명
    private String filePath;           // 파일 경로
    private Long fileSize;             // 파일 크기
    private String fileType;           // 파일 타입
    private LocalDateTime createdAt;   // 업로드 일시
    private String createdBy;          // 업로더
}