package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.request.PostRequestDTO;
import com.sesac.backend.board.dto.response.PostResponseDTO;
import com.sesac.backend.board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/boards/{boardId}/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Post", description = "게시글 관리 API")
public class PostController {
    private final PostService postService;

    // 게시글 작성
    @Operation(
        summary = "게시글 작성",
        description = "특정 게시판에 새로운 게시글을 작성합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 작성되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @PostMapping
    public ResponseEntity<?> createPost(
            @PathVariable UUID boardId,
            @RequestBody @Valid PostRequestDTO requestDTO,
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            requestDTO.setBoardId(boardId);
            log.info("Creating post for board {}: {}, userId: {}", boardId, requestDTO, userId);
            PostResponseDTO response = postService.createPost(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판의 게시글 목록 조회
    @Operation(
        summary = "게시글 목록 조회",
        description = "특정 게시판의 모든 게시글 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 목록을 성공적으로 조회했습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping
    public ResponseEntity<?> getPostsByBoard(
            @PathVariable UUID boardId) {
        try {
            log.info("Fetching posts for boardId: {}", boardId);
            List<PostResponseDTO> response = postService.getPostsByBoard(boardId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판의 특정 게시글 조회
    @Operation(
        summary = "게시글 상세 조회",
        description = "특정 게시판의 특정 게시글을 상세히 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글을 성공적으로 조회했습니다."),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
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
            HttpStatus status = e instanceof EntityNotFoundException
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판의 특정 게시글 수정
    @Operation(
        summary = "게시글 수정",
        description = "특정 게시판의 특정 게시글을 수정합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 수정되었습니다."),
        @ApiResponse(responseCode = "403", description = "권한이 없어서 수정할 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestBody @Valid PostRequestDTO requestDTO,
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            requestDTO.setBoardId(boardId);
            log.info("Updating post {} in board {}", postId, boardId);
            PostResponseDTO response = postService.updatePost(postId, requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            // 권한 없음 예외 처리
            log.error("Permission denied for updating post", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            // 엔티티를 찾을 수 없는 경우
            log.error("Entity not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판의 특정 게시글 삭제
    @Operation(
        summary = "게시글 삭제",
        description = "특정 게시판의 특정 게시글을 삭제합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 삭제되었습니다."),
        @ApiResponse(responseCode = "403", description = "권한이 없어서 삭제할 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            log.info("Deleting post {} from board {}", postId, boardId);
            postService.deletePost(postId, userId);
            return ResponseEntity.ok(Map.of("message", "게시글이 성공적으로 삭제되었습니다."));
        } catch (IllegalStateException e) {
            // 권한 없음 예외 처리
            log.error("Permission denied for deleting post", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            // 엔티티를 찾을 수 없는 경우
            log.error("Entity not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판의 게시글 수 조회
    @Operation(
        summary = "게시글 수 조회",
        description = "특정 게시판의 게시글 수를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 수를 성공적으로 조회했습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping("/count")
    public ResponseEntity<?> getPostCount(
            @PathVariable UUID boardId) {
        try {
            log.info("Counting posts for board: {}", boardId);
            long count = postService.getPostCount(boardId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            log.error("Error counting posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }
}