package com.sesac.backend.aws.dto;

import lombok.*;

@Getter
@Builder
@ToString(exclude = {"fileContent"})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDownloadResponse {

    private byte[] fileContent;        // 파일 내용
    private String originalFileName;    // 원본 파일명
    private String contentType;        // 파일 타입
    private long size;                 // 파일 크기
}
