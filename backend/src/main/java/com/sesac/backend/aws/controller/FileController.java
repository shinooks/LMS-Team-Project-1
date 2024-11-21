package com.sesac.backend.aws.controller;

import com.sesac.backend.aws.dto.FileDownloadResponse;
import com.sesac.backend.aws.dto.FileUploadResponse;
import com.sesac.backend.aws.service.S3Service;
import com.sesac.backend.board.repository.UserAuthenticationRepository;
import com.sesac.backend.entity.UserAuthentication;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
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
    private final UserAuthenticationRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId) {
        try {
            UserAuthentication user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

            FileUploadResponse response = s3Service.uploadFile(file, user.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/download/{savedFileName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String savedFileName,
            @RequestParam("userId") UUID userId) {
        try {
            UserAuthentication user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

            FileDownloadResponse response = s3Service.downloadFile(savedFileName, user.getName());

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
    public ResponseEntity<Void> deleteFile(
            @PathVariable String savedFileName,
            @RequestParam("userId") UUID userId) {
        try {
            UserAuthentication user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

            s3Service.deleteFile(savedFileName, user.getName());
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}