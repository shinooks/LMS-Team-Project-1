package com.sesac.backend.aws.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileUploadResponse {

    private String originalFileName;    // 원본 파일명
    private String savedFileName;       // 저장된 파일명 (UUID 포함)
    private String fileUrl;            // 파일 접근 URL
    private String contentType;        // 파일 타입
    private long size;                 // 파일 크기
}