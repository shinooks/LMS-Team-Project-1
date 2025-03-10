package com.sesac.backend.board.controller;
import com.sesac.backend.board.constant.BoardConstants;
import com.sesac.backend.board.constant.BoardType;
import com.sesac.backend.board.dto.request.PostRequestDTO;
import com.sesac.backend.board.dto.request.PostSearchRequestDto;
import com.sesac.backend.board.dto.response.PostResponseDTO;
import com.sesac.backend.board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping(BoardConstants.Post.API_POST_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = BoardConstants.Post.SWAGGER_TAG_NAME,
        description = BoardConstants.Post.SWAGGER_TAG_DESCRIPTION)
public class PostController {
    private final PostService postService;

    // 게시글 등록
    @Operation(
            summary = BoardConstants.Post.API_OPERATION_CREATE_SUMMARY,
            description = BoardConstants.Post.API_OPERATION_CREATE_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = BoardConstants.Post.SUCCESS_CREATE),
            @ApiResponse(responseCode = "400", description = BoardConstants.Common.ERROR_INVALID_REQUEST),
            @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    })
    @PostMapping
    public ResponseEntity<?> createPost(
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_BOARD_ID)
            @PathVariable UUID boardId,
            @RequestBody @Valid PostRequestDTO requestDTO,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            requestDTO.setBoardId(boardId);
            log.info(BoardConstants.Post.LOG_CREATE, boardId, requestDTO, userId);
            PostResponseDTO response = postService.createPost(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Post.LOG_ERROR_CREATE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 목록 조회 (페이징 + 검색)
    @Operation(
            summary = BoardConstants.Post.API_OPERATION_SEARCH_SUMMARY,
            description = BoardConstants.Post.API_OPERATION_SEARCH_DESCRIPTION
    )
    @GetMapping
    public ResponseEntity<?> getPosts(
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_BOARD_ID)
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

            log.info(BoardConstants.Post.LOG_SEARCH, searchDto);
            Page<PostResponseDTO> response = postService.searchPosts(
                    boardId,
                    searchDto,
                    pageable
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Post.LOG_ERROR_SEARCH, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 상세 조회
    @Operation(
            summary = BoardConstants.Post.API_OPERATION_GET_SUMMARY,
            description = BoardConstants.Post.API_OPERATION_GET_DESCRIPTION
    )
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_BOARD_ID)
            @PathVariable UUID boardId,
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_POST_ID)
            @PathVariable UUID postId) {
        try {
            log.info(BoardConstants.Post.LOG_GET, postId, boardId);
            PostResponseDTO response = postService.getPost(postId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.Post.LOG_ERROR_GET, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 수정
    @Operation(
            summary = BoardConstants.Post.API_OPERATION_UPDATE_SUMMARY,
            description = BoardConstants.Post.API_OPERATION_UPDATE_DESCRIPTION
    )
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_BOARD_ID)
            @PathVariable UUID boardId,
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_POST_ID)
            @PathVariable UUID postId,
            @RequestBody @Valid PostRequestDTO requestDTO,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            requestDTO.setBoardId(boardId);
            log.info(BoardConstants.Post.LOG_UPDATE, postId, requestDTO);
            PostResponseDTO response = postService.updatePost(postId, requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.Post.LOG_ERROR_UPDATE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 삭제
    @Operation(
            summary = BoardConstants.Post.API_OPERATION_DELETE_SUMMARY,
            description = BoardConstants.Post.API_OPERATION_DELETE_DESCRIPTION
    )
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_BOARD_ID)
            @PathVariable UUID boardId,
            @Parameter(description = BoardConstants.Post.SWAGGER_PARAM_POST_ID)
            @PathVariable UUID postId,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            log.info(BoardConstants.Post.LOG_DELETE, postId);
            postService.deletePost(postId, userId);
            return ResponseEntity.ok(Map.of("message", BoardConstants.Post.SUCCESS_DELETE));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.Post.LOG_ERROR_DELETE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}