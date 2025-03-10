package com.sesac.backend.board.service;

import com.sesac.backend.board.constant.BoardConstants;
import com.sesac.backend.board.constant.UserType;
import com.sesac.backend.board.dto.request.BoardRequestDTO;
import com.sesac.backend.board.dto.response.BoardResponseDTO;
import com.sesac.backend.board.repository.BoardRepository;
import com.sesac.backend.board.repository.PostRepository;
import com.sesac.backend.board.repository.UserAuthenticationRepository;
import com.sesac.backend.entity.Board;
import com.sesac.backend.entity.UserAuthentication;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserAuthenticationRepository userRepository;

    // 게시판 생성
    @Transactional
    public BoardResponseDTO createBoard(BoardRequestDTO requestDTO, UUID userId) {
        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        // 교직원 권한 체크
        if (user.getUserType() != UserType.STAFF) {
            throw new IllegalStateException(BoardConstants.Board.ERROR_NO_PERMISSION_CREATE);
        }

        // 게시판 이름 중복 검사
        if (boardRepository.existsByBoardName(requestDTO.getBoardName())) {
            throw new IllegalStateException(BoardConstants.Board.ERROR_DUPLICATE_NAME);
        }

        Board board = Board.builder()
                .boardName(requestDTO.getBoardName())
                .boardType(requestDTO.getBoardType())
                .allowAnonymous(requestDTO.isAllowAnonymous())
                .allowComments(requestDTO.isAllowComments())
                .allowEdit(requestDTO.isAllowEdit())
                .allowDelete(requestDTO.isAllowDelete())
                .build();

        board.setCreatedBy(user.getName());

        Board savedBoard = boardRepository.save(board);
        return convertToResponseDTO(savedBoard);
    }

    // 게시판 수정
    @Transactional
    public BoardResponseDTO updateBoard(UUID boardId, BoardRequestDTO requestDTO, UUID userId) {
        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        // 교직원 권한 체크
        if (user.getUserType() != UserType.STAFF) {
            throw new IllegalStateException(BoardConstants.Board.ERROR_NO_PERMISSION_UPDATE);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Board.ERROR_NOT_FOUND));

        board.setBoardName(requestDTO.getBoardName());
        board.setBoardType(requestDTO.getBoardType());
        board.setAllowAnonymous(requestDTO.isAllowAnonymous());
        board.setAllowComments(requestDTO.isAllowComments());
        board.setAllowEdit(requestDTO.isAllowEdit());
        board.setAllowDelete(requestDTO.isAllowDelete());

        board.setUpdatedBy(user.getName());

        return convertToResponseDTO(board);
    }

    // 게시판 삭제
    @Transactional
    public void deleteBoard(UUID boardId, UUID userId) {
        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        // 교직원 권한 체크
        if (user.getUserType() != UserType.STAFF) {
            throw new IllegalStateException(BoardConstants.Board.ERROR_NO_PERMISSION_DELETE);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Board.ERROR_NOT_FOUND));

        // 게시판 삭제 권한 확인
        if (!board.isAllowDelete()) {
            throw new IllegalStateException(BoardConstants.Board.ERROR_DELETE_NOT_ALLOWED);
        }

        // 게시판과 관련된 모든 게시글이 함께 삭제됨 (cascade)
        boardRepository.delete(board);
    }

    // 게시판 조회
    public BoardResponseDTO getBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Board.ERROR_NOT_FOUND));
        return convertToResponseDTO(board);
    }

    // 게시판 목록 조회
    public List<BoardResponseDTO> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Entity -> DTO 변환
    private BoardResponseDTO convertToResponseDTO(Board board) {
        return BoardResponseDTO.builder()
                .boardId(board.getBoardId())
                .boardName(board.getBoardName())
                .boardType(board.getBoardType())
                .allowAnonymous(board.isAllowAnonymous())
                .allowComments(board.isAllowComments())
                .allowEdit(board.isAllowEdit())
                .allowDelete(board.isAllowDelete())
                .createdAt(board.getCreatedAt())
                .createdBy(board.getCreatedBy())
                .build();
    }
}