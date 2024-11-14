package com.sesac.backend.board.controller;
import com.sesac.backend.board.constant.BoardType;
import com.sesac.backend.board.dto.request.PostRequestDTO;
import com.sesac.backend.board.dto.request.PostSearchRequestDto;
import com.sesac.backend.board.dto.response.PostResponseDTO;
import com.sesac.backend.board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Post", description = "게시글 관리 API")
public class PostController {
    private final PostService postService;

    // 게시글 등록
    @Operation(summary = "게시글 작성", description = "특정 게시판에 새로운 게시글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 작성되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
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

    // 게시글 목록 조회 (페이징 + 검색)
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 페이징하여 조회하며, 검색 조건을 적용할 수 있습니다.")
    @GetMapping
    public ResponseEntity<?> getPosts(
            @PathVariable UUID boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute PostSearchRequestDto searchDto) {
        try {
            Sort sort = Sort.by(
                    Sort.Direction.fromString(searchDto.getSortDirection()),
                    searchDto.getSortBy()
            );
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<PostResponseDTO> response = postService.searchPosts(
                    boardId,
                    searchDto,
                    pageable
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 상세 조회
    @Operation(summary = "게시글 상세 조회", description = "특정 게시글을 상세히 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId) {
        try {
            log.info("Fetching post {} from board {}", postId, boardId);
            PostResponseDTO response = postService.getPost(postId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error fetching post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "특정 게시글을 수정합니다.")
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestBody @Valid PostRequestDTO requestDTO,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            requestDTO.setBoardId(boardId);
            PostResponseDTO response = postService.updatePost(postId, requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable UUID boardId,
            @PathVariable UUID postId,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            postService.deletePost(postId, userId);
            return ResponseEntity.ok(Map.of("message", "게시글이 성공적으로 삭제되었습니다."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}