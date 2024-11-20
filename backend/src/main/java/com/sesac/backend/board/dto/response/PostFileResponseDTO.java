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
    // PostFile 고유 필드
    private UUID fileId;               // 파일 ID
    private UUID postId;               // 게시글 ID

    // BaseFile 상속 필드들
    private String originalName;       // 원본 파일명
    private String storedName;         // 저장된 파일명
    private String filePath;           // 파일 경로
    private Long fileSize;             // 파일 크기 (bytes)
    private String fileType;           // 파일 MIME 타입

    // BaseEntity 상속 필드들
    private LocalDateTime createdAt;   // 생성 일시
    private LocalDateTime updatedAt;   // 수정 일시
    private String createdBy;          // 생성자
    private String updatedBy;          // 수정자

    // 삭제 관련 필드 추가
    private boolean isDeleted;         // 삭제 여부
    private LocalDateTime deletedAt;   // 삭제 일시
}