package com.sesac.backend.board.controller;

import com.sesac.backend.board.dto.request.BoardRequestDTO;
import com.sesac.backend.board.dto.response.BoardResponseDTO;
import com.sesac.backend.board.service.BoardService;
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
public class BoardController {

    private final BoardService boardService;

    // 게시판 생성
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardRequestDTO requestDTO) {
        try {
            log.info("Creating board: {}", requestDTO);
            BoardResponseDTO response = boardService.createBoard(requestDTO);
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
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable UUID boardId,
            @RequestBody @Valid BoardRequestDTO requestDTO) {
        try {
            log.info("Updating board: {}", requestDTO);
            BoardResponseDTO response = boardService.updateBoard(boardId, requestDTO);
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
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable UUID boardId) {
        try {
            log.info("Deleting board with id: {}", boardId);
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok(Map.of("message", "게시판이 성공적으로 삭제되었습니다."));
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