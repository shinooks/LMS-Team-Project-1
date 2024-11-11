package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.request.CommentRequestDTO;
import com.sesac.backend.board.dto.response.CommentResponseDTO;
import com.sesac.backend.board.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestBody @Valid CommentRequestDTO requestDTO,
            @RequestParam UUID userId) {
        try {
            log.info("Creating comment for post {}: {}", postId, requestDTO);
            requestDTO.setPostId(postId); // URL의 postId를 DTO에 설정
            CommentResponseDTO response = commentService.createComment(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable UUID commentId,
            @RequestBody @Valid CommentRequestDTO requestDTO) {
        try {
            log.info("Updating comment {}: {}", commentId, requestDTO);
            CommentResponseDTO response = commentService.updateComment(commentId, requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 댓글 삭제
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
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글의 댓글 목록 조회
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
                    .body(Map.of("message", e.getMessage()));
        }
    }
}