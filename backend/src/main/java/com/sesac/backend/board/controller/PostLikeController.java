package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.response.PostLikeResponseDTO;
import com.sesac.backend.board.service.PostLikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/posts/{postId}/likes")
@RequiredArgsConstructor
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 좋아요 토글 (생성/삭제)
    @PostMapping
    public ResponseEntity<?> toggleLike(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            log.info("Toggling like for post {} by user {}", postId, userId);
            PostLikeResponseDTO response = postLikeService.toggleLike(postId, userId);

            // 응답 메시지 생성
            String message = response == null ? "좋아요가 취소되었습니다." : "좋아요가 추가되었습니다.";

            // response가 null이어도 정상 응답으로 처리
            return ResponseEntity.ok(Map.of(
                    "message", message,
                    "data", response != null ? response : Collections.emptyMap()
            ));

        } catch (IllegalStateException e) {
            log.error("Permission denied for toggling like: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            log.error("Entity not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error toggling like", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "좋아요 처리 중 오류가 발생했습니다."));
        }
    }

    // 게시글의 좋아요 수 조회
    @GetMapping("/count")
    public ResponseEntity<?> getLikeCount(
            @PathVariable UUID postId) {
        try {
            long likeCount = postLikeService.getLikeCount(postId);
            return ResponseEntity.ok(Map.of("likeCount", likeCount));
        } catch (Exception e) {
            log.error("Error getting like count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 사용자의 좋아요 여부 확인
    @GetMapping("/check")
    public ResponseEntity<?> hasUserLiked(
            @PathVariable UUID postId,
            @RequestHeader("X-USER-ID") UUID userId) {  // RequestParam -> RequestHeader
        try {
            boolean hasLiked = postLikeService.hasUserLiked(postId, userId);
            return ResponseEntity.ok(Map.of("hasLiked", hasLiked));
        } catch (Exception e) {
            log.error("Error checking like status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
