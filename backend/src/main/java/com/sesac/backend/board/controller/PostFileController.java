package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.request.PostFileRequestDTO;
import com.sesac.backend.board.dto.response.PostFileResponseDTO;
import com.sesac.backend.board.service.PostFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/posts/{postId}/files")
@RequiredArgsConstructor
@Slf4j
public class PostFileController {

    private final PostFileService postFileService;

    // 파일 업로드
    @PostMapping
    public ResponseEntity<?> uploadFile(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading file for post {}: {}", postId, file.getOriginalFilename());

            PostFileRequestDTO requestDTO = PostFileRequestDTO.builder()
                    .postId(postId)
                    .originalName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .build();

            PostFileResponseDTO response = postFileService.uploadFile(requestDTO, file.getBytes());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error uploading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 파일 다운로드
    @GetMapping("/{fileId}")
    public ResponseEntity<?> downloadFile(
            @PathVariable UUID fileId) {
        try {
            PostFileResponseDTO fileInfo = postFileService.getFileInfo(fileId);
            byte[] fileData = postFileService.downloadFile(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileInfo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileInfo.getOriginalName() + "\"")
                    .body(fileData);
        } catch (Exception e) {
            log.error("Error downloading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글의 첨부파일 목록 조회
    @GetMapping
    public ResponseEntity<?> getFilesByPost(
            @PathVariable UUID postId) {
        try {
            List<PostFileResponseDTO> files = postFileService.getFilesByPost(postId);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            log.error("Error fetching files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(
            @PathVariable UUID fileId) {
        try {
            postFileService.deleteFile(fileId);
            return ResponseEntity.ok(Map.of("message", "파일이 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}