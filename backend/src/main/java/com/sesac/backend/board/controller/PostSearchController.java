package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.response.PostResponseDTO;
import com.sesac.backend.board.service.PostService;
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
public class PostSearchController {
    private final PostService postService;

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
                    .body(Map.of("message", e.getMessage()));
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
                    .body(Map.of("message", e.getMessage()));
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
                    .body(Map.of("message", e.getMessage()));
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
                    .body(Map.of("message", e.getMessage()));
        }
    }
}