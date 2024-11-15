package com.sesac.backend.aws.controller;

import com.sesac.backend.aws.dto.FileDownloadResponse;
import com.sesac.backend.aws.dto.FileUploadResponse;
import com.sesac.backend.aws.service.S3Service;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileUploadResponse response = s3Service.uploadFile(file);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/download/{savedFileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String savedFileName) {
        try {
            FileDownloadResponse response = s3Service.downloadFile(savedFileName);

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(response.getContentType()))
                .contentLength(response.getSize())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    ContentDisposition.attachment()
                        .filename(response.getOriginalFileName(), StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .body(response.getFileContent());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{savedFileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String savedFileName) {
        try {
            s3Service.deleteFile(savedFileName);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}