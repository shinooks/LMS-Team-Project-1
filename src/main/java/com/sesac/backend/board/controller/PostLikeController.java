package com.sesac.backend.board.controller;

import com.sesac.backend.board.constant.BoardConstants;
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
@RequestMapping(BoardConstants.PostLike.API_POST_LIKES)
@RequiredArgsConstructor
@Slf4j
@Tag(name = BoardConstants.PostLike.SWAGGER_TAG_NAME,
        description = BoardConstants.PostLike.SWAGGER_TAG_DESCRIPTION)
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 좋아요 토글 생성 삭제
    @Operation(
            summary = BoardConstants.PostLike.API_OPERATION_TOGGLE_SUMMARY,
            description = BoardConstants.PostLike.API_OPERATION_TOGGLE_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = BoardConstants.PostLike.SUCCESS_TOGGLE),
            @ApiResponse(responseCode = "403", description = BoardConstants.Common.ERROR_NO_PERMISSION),
            @ApiResponse(responseCode = "404", description = BoardConstants.PostLike.ERROR_POST_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    })
    @PostMapping
    public ResponseEntity<?> toggleLike(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID, required = true)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            log.info(BoardConstants.PostLike.LOG_TOGGLE, postId, userId);
            PostLikeResponseDTO response = postLikeService.toggleLike(postId, userId);

            String message = response == null ?
                    BoardConstants.PostLike.SUCCESS_UNLIKE :
                    BoardConstants.PostLike.SUCCESS_LIKE;

            return ResponseEntity.ok(Map.of(
                    "message", message,
                    "data", response != null ? response : Collections.emptyMap()
            ));

        } catch (IllegalStateException e) {
            log.error(BoardConstants.PostLike.LOG_ERROR_PERMISSION, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            log.error(BoardConstants.Common.LOG_ENTITY_NOT_FOUND, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.PostLike.LOG_ERROR_TOGGLE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", BoardConstants.PostLike.ERROR_TOGGLE));
        }
    }

    // 게시글의 좋아요 수 조회
    @Operation(
            summary = BoardConstants.PostLike.API_OPERATION_COUNT_SUMMARY,
            description = BoardConstants.PostLike.API_OPERATION_COUNT_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = BoardConstants.PostLike.SUCCESS_COUNT),
            @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    })
    @GetMapping("/count")
    public ResponseEntity<?> getLikeCount(
            @PathVariable UUID postId) {
        try {
            log.info(BoardConstants.PostLike.LOG_COUNT, postId);
            long likeCount = postLikeService.getLikeCount(postId);
            return ResponseEntity.ok(Map.of("likeCount", likeCount));
        } catch (Exception e) {
            log.error(BoardConstants.PostLike.LOG_ERROR_COUNT, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 사용자의 좋아요 여부 확인
    @Operation(
            summary = BoardConstants.PostLike.API_OPERATION_CHECK_SUMMARY,
            description = BoardConstants.PostLike.API_OPERATION_CHECK_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = BoardConstants.PostLike.SUCCESS_CHECK),
            @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    })
    @GetMapping("/check")
    public ResponseEntity<?> hasUserLiked(
            @PathVariable UUID postId,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID, required = true)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            boolean hasLiked = postLikeService.hasUserLiked(postId, userId);
            return ResponseEntity.ok(Map.of("hasLiked", hasLiked));
        } catch (Exception e) {
            log.error(BoardConstants.PostLike.LOG_ERROR_CHECK, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}