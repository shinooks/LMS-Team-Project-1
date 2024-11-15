package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.request.BoardRequestDTO;
import com.sesac.backend.board.dto.response.BoardResponseDTO;
import com.sesac.backend.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Board", description = "게시판 관리 API")
public class BoardController {

    private final BoardService boardService;

    // 게시판 생성
    @Operation(
        summary = "게시판 생성",
        description = "새로운 게시판을 생성합니다."
    )
    @ApiResponse(responseCode = "200", description = "게시판이 성공적으로 생성되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PostMapping
    public ResponseEntity<?> createBoard(
            @RequestBody @Valid BoardRequestDTO requestDTO,
            @RequestHeader("X-USER-ID") UUID userId) {  // Header에서 userId를 받음
        try {
            log.info("Creating board: {}", requestDTO);
            BoardResponseDTO response = boardService.createBoard(requestDTO, userId);  // userId 전달
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating board", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시판 수정
    @Operation(
        summary = "게시판 수정",
        description = "특정 ID를 가진 게시판을 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "게시판이 성공적으로 수정되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "게시판을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable UUID boardId,
            @RequestBody @Valid BoardRequestDTO requestDTO,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            log.info("Updating board: {}", requestDTO);
            BoardResponseDTO response = boardService.updateBoard(boardId, requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating board", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시판 삭제
    @Operation(
        summary = "게시판 삭제",
        description = "특정 ID를 가진 게시판을 삭제합니다."
    )
    @ApiResponse(responseCode = "200", description = "게시판이 성공적으로 삭제되었습니다.")
    @ApiResponse(responseCode = "403", description = "권한이 없어서 삭제할 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable UUID boardId,
            @RequestHeader("X-USER-ID") UUID userId) {
        try {
            log.info("Deleting board with id: {}", boardId);
            boardService.deleteBoard(boardId, userId);
            return ResponseEntity.ok(Map.of("message", "게시판이 성공적으로 삭제되었습니다."));
        } catch (IllegalStateException e) {
            // 권한 없음 예외 처리
            log.error("Permission denied for deleting board", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting board", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시판 목록 조회
    @Operation(
        summary = "게시판 목록 조회",
        description = "모든 게시판의 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "게시판 목록을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping
    public ResponseEntity<?> getAllBoards() {
        try {
            log.info("Fetching all boards");
            List<BoardResponseDTO> response = boardService.getAllBoards();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching boards", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 게시판 상세 조회
    @Operation(
        summary = "게시판 상세 조회",
        description = "특정 ID를 가진 게시판의 상세 정보를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "게시판을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "404", description = "게시판을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable UUID boardId) {
        try {
            log.info("Fetching board with id: {}", boardId);
            BoardResponseDTO response = boardService.getBoard(boardId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching board", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }
}