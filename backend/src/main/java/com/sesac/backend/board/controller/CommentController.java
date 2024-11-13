package com.sesac.backend.board.controller;

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
@RequestMapping("/boards/{boardId}/posts/{postId}/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comment", description = "댓글 관리 API")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @Operation(
        summary = "댓글 작성",
        description = "특정 게시글에 대한 새로운 댓글을 작성합니다."
    )
    @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 작성되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestBody @Valid CommentRequestDTO requestDTO,
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-USER-ID") UUID userId)  {
        try {
            log.info("Creating comment for post {}: {}", postId, requestDTO);
            requestDTO.setPostId(postId); // URL의 postId를 DTO에 설정
            CommentResponseDTO response = commentService.createComment(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 댓글 수정
    @Operation(
        summary = "댓글 수정",
        description = "특정 ID를 가진 댓글을 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 수정되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable UUID commentId,
            @RequestBody @Valid CommentRequestDTO requestDTO,
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            log.info("Updating comment {}: {}", commentId, requestDTO);
            CommentResponseDTO response = commentService.updateComment(commentId, requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 댓글 삭제
    @Operation(
        summary = "댓글 삭제",
        description = "특정 ID를 가진 댓글을 삭제합니다."
    )
    @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 삭제되었습니다.")
    @ApiResponse(responseCode = "403", description = "권한이 없어서 삭제할 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable UUID commentId) {
        try {
            log.info("Deleting comment: {}", commentId);
            commentService.deleteComment(commentId);
            return ResponseEntity.ok(Map.of("message", "댓글이 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 게시글의 댓글 목록 조회
    @Operation(
        summary = "게시글의 댓글 목록 조회",
        description = "특정 게시글에 대한 모든 댓글을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "댓글 목록을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping
    public ResponseEntity<?> getCommentsByPost(
            @PathVariable UUID postId) {
        try {
            log.info("Fetching comments for post: {}", postId);
            List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            log.error("Error fetching comments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }
}