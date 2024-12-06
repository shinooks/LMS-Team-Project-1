package com.sesac.backend.board.controller;

import com.sesac.backend.board.constant.BoardConstants;
import com.sesac.backend.board.dto.request.BoardRequestDTO;
import com.sesac.backend.board.dto.response.BoardResponseDTO;
import com.sesac.backend.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping(BoardConstants.Board.API_BOARD_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = BoardConstants.Board.SWAGGER_TAG_NAME,
        description = BoardConstants.Board.SWAGGER_TAG_DESCRIPTION)
public class BoardController {

    private final BoardService boardService;

    // 게시판 생성
    @Operation(
            summary = BoardConstants.Board.API_OPERATION_CREATE_SUMMARY,
            description = BoardConstants.Board.API_OPERATION_CREATE_DESCRIPTION
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Board.SUCCESS_CREATE)
    @ApiResponse(responseCode = "400", description = BoardConstants.Common.ERROR_INVALID_REQUEST)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @PostMapping
    public ResponseEntity<?> createBoard(
            @RequestBody @Valid BoardRequestDTO requestDTO,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID, required = true)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            log.info(BoardConstants.Board.LOG_CREATE, requestDTO);
            BoardResponseDTO response = boardService.createBoard(requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Board.LOG_ERROR_CREATE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(),
                            "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판 수정
    @Operation(
            summary = BoardConstants.Board.API_OPERATION_UPDATE_SUMMARY,
            description = BoardConstants.Board.API_OPERATION_UPDATE_DESCRIPTION
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Board.SUCCESS_UPDATE)
    @ApiResponse(responseCode = "400", description = BoardConstants.Common.ERROR_INVALID_REQUEST)
    @ApiResponse(responseCode = "404", description = BoardConstants.Board.ERROR_NOT_FOUND)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable UUID boardId,
            @RequestBody @Valid BoardRequestDTO requestDTO,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID, required = true)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            log.info(BoardConstants.Board.LOG_UPDATE, requestDTO);
            BoardResponseDTO response = boardService.updateBoard(boardId, requestDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Board.LOG_ERROR_UPDATE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(),
                            "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판 삭제
    @Operation(
            summary = BoardConstants.Board.API_OPERATION_DELETE_SUMMARY,
            description = BoardConstants.Board.API_OPERATION_DELETE_DESCRIPTION
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Board.SUCCESS_DELETE)
    @ApiResponse(responseCode = "403", description = BoardConstants.Common.ERROR_NO_PERMISSION)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable UUID boardId,
            @Parameter(description = BoardConstants.Common.SWAGGER_PARAM_USER_ID, required = true)
            @RequestHeader(BoardConstants.Common.HEADER_USER_ID) UUID userId) {
        try {
            log.info(BoardConstants.Board.LOG_DELETE, boardId);
            boardService.deleteBoard(boardId, userId);
            return ResponseEntity.ok(Map.of("message", BoardConstants.Board.SUCCESS_DELETE));
        } catch (IllegalStateException e) {
            log.error(BoardConstants.Board.LOG_ERROR_PERMISSION, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error(BoardConstants.Board.LOG_ERROR_DELETE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(),
                            "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판 목록 조회
    @Operation(
            summary = BoardConstants.Board.API_OPERATION_LIST_SUMMARY,
            description = BoardConstants.Board.API_OPERATION_LIST_DESCRIPTION
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Board.SUCCESS_LIST)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @GetMapping
    public ResponseEntity<?> getAllBoards() {
        try {
            log.info(BoardConstants.Board.LOG_FETCH_ALL);
            List<BoardResponseDTO> response = boardService.getAllBoards();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Board.LOG_ERROR_FETCH, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(),
                            "error", e.getClass().getSimpleName()));
        }
    }

    // 게시판 상세 조회
    @Operation(
            summary = BoardConstants.Board.API_OPERATION_GET_SUMMARY,
            description = BoardConstants.Board.API_OPERATION_GET_DESCRIPTION
    )
    @ApiResponse(responseCode = "200", description = BoardConstants.Board.SUCCESS_GET)
    @ApiResponse(responseCode = "404", description = BoardConstants.Board.ERROR_NOT_FOUND)
    @ApiResponse(responseCode = "500", description = BoardConstants.Common.ERROR_INTERNAL_SERVER)
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable UUID boardId) {
        try {
            log.info(BoardConstants.Board.LOG_FETCH, boardId);
            BoardResponseDTO response = boardService.getBoard(boardId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(BoardConstants.Board.LOG_ERROR_GET, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(),
                            "error", e.getClass().getSimpleName()));
        }
    }
}