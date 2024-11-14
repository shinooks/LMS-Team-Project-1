package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.request.PostFileRequestDTO;
import com.sesac.backend.board.dto.response.PostFileResponseDTO;
import com.sesac.backend.board.service.PostFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
@Tag(name = "PostFile", description = "게시글 첨부파일 관리 API")
public class PostFileController {

    private final PostFileService postFileService;

    // 파일 업로드
    @Operation(
        summary = "파일 업로드",
        description = "특정 게시글에 새로운 파일을 업로드합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "파일이 성공적으로 업로드되었습니다.",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = PostFileResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                     content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                     content = @Content)
    })


    @PostMapping
    public ResponseEntity<?> uploadFile(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestHeader("X-USER-ID") UUID userId,  // userId 받아오기
            @RequestParam("file") MultipartFile file) {
        try {
            PostFileRequestDTO requestDTO = PostFileRequestDTO.builder()
                    .postId(postId)
                    .originalName(file.getOriginalFilename())
                    .storedName(null)
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .build();

            // userId를 서비스로 전달
            PostFileResponseDTO response = postFileService.uploadFile(requestDTO, file.getBytes(), userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error uploading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 파일 다운로드
    @Operation(
        summary = "파일 다운로드",
        description = "특정 파일을 다운로드합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "파일이 성공적으로 다운로드되었습니다.",
                     content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없습니다.",
                     content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                     content = @Content)
    })
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
        } catch (EntityNotFoundException e) {
            log.error("File not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error downloading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글의 첨부파일 목록 조회
    @Operation(
        summary = "게시글의 첨부파일 목록 조회",
        description = "특정 게시글에 대한 모든 첨부파일 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "첨부파일 목록을 성공적으로 조회했습니다.",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = PostFileResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                     content = @Content)
    })
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
    @Operation(
        summary = "파일 삭제",
        description = "특정 파일을 삭제합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "파일이 성공적으로 삭제되었습니다.",
                     content = @Content),
        @ApiResponse(responseCode = "403", description = "권한이 없어서 삭제할 수 없습니다.",
                     content = @Content),
        @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없습니다.",
                     content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                     content = @Content)
    })
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(
            @PathVariable UUID fileId) {
        try {
            postFileService.deleteFile(fileId);
            return ResponseEntity.ok(Map.of("message", "파일이 성공적으로 삭제되었습니다."));
        } catch (IllegalStateException e) {
            // 권한 없음 예외 처리
            log.error("Permission denied for deleting file", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            // 엔티티를 찾을 수 없는 경우
            log.error("File not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}