package com.sesac.backend.aws.service;

import com.sesac.backend.aws.dto.FileDownloadResponse;
import com.sesac.backend.aws.dto.FileUploadResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 업로드
    public FileUploadResponse uploadFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String savedFileName = createFileName(originalFileName);

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(savedFileName)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return FileUploadResponse.builder()
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .fileUrl(getFileUrl(savedFileName))
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    // 파일 다운로드
    public FileDownloadResponse downloadFile(String savedFileName) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(savedFileName)
                .build();

            ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);
            String originalFileName = getOriginalName(savedFileName);

            return FileDownloadResponse.builder()
                .fileContent(response.asByteArray())
                .originalFileName(originalFileName)
                .contentType(response.response().contentType())
                .size(response.response().contentLength())
                .build();

        } catch (S3Exception e) {
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }
    
    // 파일 삭제
    public void deleteFile(String fileName) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

            s3Client.deleteObject(request);
            log.info("File deleted successfully: {}", fileName);

        } catch (S3Exception e) {
            log.error("Failed to delete file: {}", fileName, e);
            throw new RuntimeException("파일 삭제 실패: " + fileName);
        }
    }

    private String createFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    private String getOriginalName(String savedFileName) {
        return savedFileName.substring(savedFileName.lastIndexOf('_') + 1);
    }

    private String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
            bucketName, "ap-northeast-2", fileName);
    }
}