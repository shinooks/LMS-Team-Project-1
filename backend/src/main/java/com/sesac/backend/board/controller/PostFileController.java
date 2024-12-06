package com.sesac.backend.board.controller;

import com.sesac.backend.board.constant.BoardConstants;
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
@RequestMapping(BoardConstants.PostFile.API_POST_FILES)
@RequiredArgsConstructor
@Slf4j
@Tag(name = BoardConstants.PostFile.SWAGGER_TAG_NAME,
        description = BoardConstants.PostFile.SWAGGER_TAG_DESCRIPTION)
public class PostFileController {

    private final PostFileService postFileService;

    // 파일 업로드
    @Operation(
            summary = "파일 업로드",
            description = BoardConstants.PostFile.API_OPERATION_UPLOAD
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = BoardConstants.PostFile.SUCCESS_UPLOAD,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostFileResponseDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = BoardConstants.Common.ERROR_INVALID_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = BoardConstants.PostFile.ERROR_POST_NOT_FOUND,
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = BoardConstants.Common.ERROR_INTERNAL_SERVER,
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> uploadFile(
            @PathVariable UUID postId,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId,
            @Parameter(description = BoardConstants.PostFile.SWAGGER_PARAM_FILE_DESC)
            @RequestParam(BoardConstants.Common.PARAM_FILE) MultipartFile file) {
        try {
            PostFileRequestDTO requestDTO = PostFileRequestDTO.builder()
                    .postId(postId)
                    .originalName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .build();

            PostFileResponseDTO response = postFileService.uploadFile(requestDTO, file.getBytes(), userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error(BoardConstants.Common.LOG_INVALID_FILE, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            log.error(BoardConstants.Common.LOG_ENTITY_NOT_FOUND, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.PostFile.LOG_FILE_UPLOAD_ERROR, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", BoardConstants.PostFile.ERROR_FILE_UPLOAD));
        }
    }

    // 파일 다운로드
    @Operation(
            summary = "파일 다운로드",
            description = BoardConstants.PostFile.API_OPERATION_DOWNLOAD
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = BoardConstants.PostFile.SUCCESS_DOWNLOAD,
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404",
                    description = BoardConstants.PostFile.ERROR_FILE_NOT_FOUND,
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = BoardConstants.Common.ERROR_INTERNAL_SERVER,
                    content = @Content)
    })
    @GetMapping("/{fileId}")
    public ResponseEntity<?> downloadFile(
            @Parameter(description = BoardConstants.PostFile.SWAGGER_PARAM_FILE_ID)
            @PathVariable UUID fileId,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            PostFileResponseDTO fileInfo = postFileService.getFileInfo(fileId);
            byte[] fileData = postFileService.downloadFile(fileId, userId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileInfo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            String.format(BoardConstants.Common.HEADER_ATTACHMENT_FORMAT,
                                    fileInfo.getOriginalName()))
                    .body(fileData);
        } catch (EntityNotFoundException e) {
            log.error(BoardConstants.PostFile.LOG_FILE_NOT_FOUND, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.PostFile.LOG_ERROR_DOWNLOADING, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글의 첨부파일 목록 조회
    @Operation(
            summary = "게시글의 첨부파일 목록 조회",
            description = BoardConstants.PostFile.API_OPERATION_LIST
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = BoardConstants.PostFile.SUCCESS_LIST,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostFileResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = BoardConstants.PostFile.ERROR_POST_NOT_FOUND,
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = BoardConstants.Common.ERROR_INTERNAL_SERVER,
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> getFilesByPost(
            @Parameter(description = BoardConstants.PostFile.SWAGGER_PARAM_BOARD_ID)
            @PathVariable UUID boardId,
            @Parameter(description = BoardConstants.PostFile.SWAGGER_PARAM_POST_ID)
            @PathVariable UUID postId) {
        try {
            List<PostFileResponseDTO> files = postFileService.getFilesByPost(postId);
            return ResponseEntity.ok(files);
        } catch (EntityNotFoundException e) {
            log.error(BoardConstants.PostFile.LOG_FILE_NOT_FOUND, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.PostFile.LOG_ERROR_FETCHING, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 파일 삭제
    @Operation(
            summary = "파일 삭제",
            description = BoardConstants.PostFile.API_OPERATION_DELETE
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = BoardConstants.PostFile.SUCCESS_FILE_DELETE,
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = BoardConstants.Common.ERROR_NO_PERMISSION,
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = BoardConstants.PostFile.ERROR_FILE_NOT_FOUND,
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = BoardConstants.Common.ERROR_INTERNAL_SERVER,
                    content = @Content)
    })
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(
            @Parameter(description = BoardConstants.PostFile.SWAGGER_PARAM_FILE_ID)
            @PathVariable UUID fileId,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            postFileService.deleteFile(fileId, userId);
            return ResponseEntity.ok(Map.of("message", BoardConstants.PostFile.SUCCESS_FILE_DELETE));
        } catch (IllegalStateException e) {
            log.error(BoardConstants.PostFile.LOG_ERROR_DELETING, e);
            HttpStatus status = e.getMessage().contains(BoardConstants.PostFile.ERROR_ALREADY_DELETED)
                    ? HttpStatus.BAD_REQUEST
                    : HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            log.error(BoardConstants.PostFile.LOG_FILE_NOT_FOUND, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.PostFile.LOG_ERROR_DELETING, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}