package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.response.PostResponseDTO;
import com.sesac.backend.board.service.PostService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "PostSearch", description = "게시글 검색 API")
public class PostSearchController {
    private final PostService postService;

    // 게시글 제목 검색
    @Operation(
        summary = "게시글 제목 검색",
        description = "특정 키워드를 포함하는 게시글의 제목을 검색합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 제목을 성공적으로 검색했습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping("/search/title")
    public ResponseEntity<?> searchPostsByTitle(
            @Parameter(description = "검색할 키워드", required = true)
            @RequestParam String keyword) {
        try {
            log.info("Searching posts by title: {}", keyword);
            List<PostResponseDTO> response = postService.searchPostsByTitle(keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching posts by title", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 게시글 내용 검색
    @Operation(
        summary = "게시글 내용 검색",
        description = "특정 키워드를 포함하는 게시글의 내용을 검색합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 내용을 성공적으로 검색했습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping("/search/content")
    public ResponseEntity<?> searchPostsByContent(
            @Parameter(description = "검색할 키워드", required = true)
            @RequestParam String keyword) {
        try {
            log.info("Searching posts by content: {}", keyword);
            List<PostResponseDTO> response = postService.searchPostsByContent(keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching posts by content", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 작성자별 게시글 조회
    @Operation(
        summary = "작성자별 게시글 조회",
        description = "특정 사용자가 작성한 모든 게시글을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작성자별 게시글을 성공적으로 조회했습니다."),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByAuthor(
            @Parameter(description = "작성자 사용자 ID", required = true)
            @PathVariable UUID userId) {
        try {
            log.info("Fetching posts by author: {}", userId);
            List<PostResponseDTO> response = postService.getPostsByAuthor(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts by author", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 특정 기간 내 게시글 조회
    @Operation(
        summary = "특정 기간 내 게시글 조회",
        description = "주어진 시작일과 종료일 사이에 작성된 모든 게시글을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "특정 기간 내 게시글을 성공적으로 조회했습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식"),
        @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    })
    @GetMapping("/search/date")
    public ResponseEntity<?> getPostsByDateRange(
            @Parameter(description = "검색 시작일시 (ISO 8601 형식)", required = true, example = "2023-10-01T00:00:00")
            @RequestParam LocalDateTime start,
            @Parameter(description = "검색 종료일시 (ISO 8601 형식)", required = true, example = "2023-10-31T23:59:59")
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