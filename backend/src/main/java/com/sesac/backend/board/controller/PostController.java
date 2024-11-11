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
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;


    // 게시글 작성
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody @Valid PostRequestDTO requestDTO,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            log.info("Creating post: {}, userId: {}", requestDTO, userId);
            PostResponseDTO response = postService.createPost(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<?> getPostsByBoard(@RequestParam(required = false) UUID boardId) {
        try {
            log.info("Fetching posts for boardId: {}", boardId);
            List<PostResponseDTO> response = postService.getPostsByBoard(boardId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable UUID postId) {
        try {
            log.info("Fetching post: {}", postId);
            PostResponseDTO response = postService.getPost(postId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable UUID postId,
            @RequestBody @Valid PostRequestDTO requestDTO) {
        try {
            log.info("Updating post: {}", postId);
            PostResponseDTO response = postService.updatePost(postId, requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시글 제목 검색
    @GetMapping("/search/title")
    public ResponseEntity<?> searchPostsByTitle(@RequestParam String keyword) {
        try {
            log.info("Searching posts by title: {}", keyword);
            List<PostResponseDTO> response = postService.searchPostsByTitle(keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시글 내용 검색
    @GetMapping("/search/content")
    public ResponseEntity<?> searchPostsByContent(@RequestParam String keyword) {
        try {
            log.info("Searching posts by content: {}", keyword);
            List<PostResponseDTO> response = postService.searchPostsByContent(keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 작성자별 게시글 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByAuthor(@PathVariable UUID userId) {
        try {
            log.info("Fetching posts by author: {}", userId);
            List<PostResponseDTO> response = postService.getPostsByAuthor(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 특정 기간 내 게시글 조회
    @GetMapping("/search/date")
    public ResponseEntity<?> getPostsByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        try {
            log.info("Fetching posts between dates: {} and {}", start, end);
            List<PostResponseDTO> response = postService.getPostsByDateRange(start, end);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts by date range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시판별 게시글 수 조회
    @GetMapping("/count/{boardId}")
    public ResponseEntity<?> getPostCount(@PathVariable UUID boardId) {
        try {
            log.info("Counting posts for board: {}", boardId);
            long count = postService.getPostCount(boardId);  // Service로 위임
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            log.error("Error counting posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {
        try {
            log.info("Deleting post: {}", postId);
            postService.deletePost(postId);
            return ResponseEntity.ok(Map.of("message", "게시글이 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }
}