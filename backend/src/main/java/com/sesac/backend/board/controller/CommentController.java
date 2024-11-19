package com.sesac.backend.board.controller;

import com.sesac.backend.board.constant.BoardConstants;
import com.sesac.backend.board.dto.request.CommentRequestDTO;
import com.sesac.backend.board.dto.response.CommentResponseDTO;
import com.sesac.backend.board.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(BoardConstants.Comment.API_COMMENT_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = BoardConstants.Comment.SWAGGER_TAG_NAME,
        description = BoardConstants.Comment.SWAGGER_TAG_DESCRIPTION)
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @Operation(
            summary = "댓글 작성",
            description = BoardConstants.Comment.API_OPERATION_CREATE
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Comment.SUCCESS_CREATE)
    @ApiResponse(responseCode = "400", description = BoardConstants.Common.ERROR_INVALID_REQUEST)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestBody @Valid CommentRequestDTO requestDTO,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID, required = true)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId)  {
        try {
            log.info(BoardConstants.Comment.LOG_CREATE, postId, requestDTO);
            requestDTO.setPostId(postId);
            CommentResponseDTO response = commentService.createComment(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Comment.LOG_ERROR_CREATE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 댓글 수정
    @Operation(
            summary = "댓글 수정",
            description = BoardConstants.Comment.API_OPERATION_UPDATE
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Comment.SUCCESS_UPDATE)
    @ApiResponse(responseCode = "400", description = BoardConstants.Common.ERROR_INVALID_REQUEST)
    @ApiResponse(responseCode = "404", description = BoardConstants.Comment.ERROR_NOT_FOUND)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable UUID commentId,
            @RequestBody @Valid CommentRequestDTO requestDTO,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID, required = true)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            log.info(BoardConstants.Comment.LOG_UPDATE, commentId, requestDTO);
            CommentResponseDTO response = commentService.updateComment(commentId, requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Comment.LOG_ERROR_UPDATE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 댓글 삭제
    @Operation(
            summary = "댓글 삭제",
            description = BoardConstants.Comment.API_OPERATION_DELETE
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Comment.SUCCESS_DELETE)
    @ApiResponse(responseCode = "403", description = BoardConstants.Comment.ERROR_NO_PERMISSION)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable UUID commentId) {
        try {
            log.info(BoardConstants.Comment.LOG_DELETE, commentId);
            commentService.deleteComment(commentId);
            return ResponseEntity.ok(Map.of("message", BoardConstants.Comment.SUCCESS_DELETE));
        } catch (Exception e) {
            log.error(BoardConstants.Comment.LOG_ERROR_DELETE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 게시글의 댓글 목록 조회
    @Operation(
            summary = "게시글의 댓글 목록 조회",
            description = BoardConstants.Comment.API_OPERATION_LIST
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Comment.SUCCESS_LIST)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @GetMapping
    public ResponseEntity<?> getCommentsByPost(
            @PathVariable UUID postId) {
        try {
            log.info(BoardConstants.Comment.LOG_FETCH, postId);
            List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            log.error(BoardConstants.Comment.LOG_ERROR_FETCH, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }
}