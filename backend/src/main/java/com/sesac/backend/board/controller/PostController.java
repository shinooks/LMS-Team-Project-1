package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.request.PostRequestDTO;
import com.sesac.backend.board.dto.response.PostResponseDTO;
import com.sesac.backend.board.repository.BoardRepository;
import com.sesac.backend.board.repository.PostRepository;
import com.sesac.backend.board.service.PostService;
import com.sesac.backend.entity.Board;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<?> createPost(
            @PathVariable UUID boardId,
            @RequestBody @Valid PostRequestDTO requestDTO,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            requestDTO.setBoardId(boardId);
            log.info("Creating post for board {}: {}, userId: {}", boardId, requestDTO, userId);
            PostResponseDTO response = postService.createPost(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시판의 게시글 목록 조회
    @GetMapping
    public ResponseEntity<?> getPostsByBoard(@PathVariable UUID boardId) {
        try {
            log.info("Fetching posts for boardId: {}", boardId);
            List<PostResponseDTO> response = postService.getPostsByBoard(boardId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시판의 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId) {
        try {
            log.info("Fetching post {} from board {}", postId, boardId);
            PostResponseDTO response = postService.getPost(postId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시판의 특정 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestBody @Valid PostRequestDTO requestDTO) {
        try {
            requestDTO.setBoardId(boardId);
            log.info("Updating post {} in board {}", postId, boardId);
            PostResponseDTO response = postService.updatePost(postId, requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시판의 특정 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId) {
        try {
            log.info("Deleting post {} from board {}", postId, boardId);
            postService.deletePost(postId);
            return ResponseEntity.ok(Map.of("message", "게시글이 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시판의 게시글 수 조회
    @GetMapping("/count")
    public ResponseEntity<?> getPostCount(@PathVariable UUID boardId) {
        try {
            log.info("Counting posts for board: {}", boardId);
            long count = postService.getPostCount(boardId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            log.error("Error counting posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}