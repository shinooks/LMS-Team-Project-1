package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.response.PostLikeResponseDTO;
import com.sesac.backend.board.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "PostLike", description = "게시글 좋아요 관리 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 좋아요 토글 생성 삭제
    @Operation(
        summary = "좋아요 토글",
        description = "특정 게시글에 좋아요를 추가하거나 취소합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요가 성공적으로 토글되었습니다."),
        @ApiResponse(responseCode = "403", description = "권한이 없어 좋아요를 토글할 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @PostMapping
    public ResponseEntity<?> toggleLike(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @Parameter(description = "사용자 ID", required = true)
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
    @Operation(
        summary = "게시글의 좋아요 수 조회",
        description = "특정 게시글의 좋아요 수를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 수를 성공적으로 조회했습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping("/count")
    public ResponseEntity<?> getLikeCount(
            @PathVariable UUID postId) {
        try {
            log.info("Counting likes for post: {}", postId);
            long likeCount = postLikeService.getLikeCount(postId);
            return ResponseEntity.ok(Map.of("likeCount", likeCount));
        } catch (Exception e) {
            log.error("Error getting like count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    //사용자의 좋아요 여부 확인
    @Operation(
        summary = "사용자의 좋아요 여부 확인",
        description = "특정 사용자가 특정 게시글에 이미 좋아요를 눌렀는지 확인합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 여부를 성공적으로 조회했습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping("/check")
    public ResponseEntity<?> hasUserLiked(
            @PathVariable UUID postId,
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-USER-ID") UUID userId) {  
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
