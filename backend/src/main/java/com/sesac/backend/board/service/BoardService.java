package com.sesac.backend.board.service;

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
@Transactional(readOnly = true)  // 기본적으로 읽기 전용으로 설정
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserAuthenticationRepository userRepository;

    // 게시판 생성
    @Transactional
    public BoardResponseDTO createBoard(BoardRequestDTO requestDTO, UUID userId) {
        // 게시판 이름 중복 검사
        if (boardRepository.existsByBoardName(requestDTO.getBoardName())) {
            throw new IllegalStateException("이미 존재하는 게시판 이름입니다.");
        }

        UserAuthentication user = userRepository.findById(userId)  // 사용자 정보 조회
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Board board = Board.builder()
                .boardName(requestDTO.getBoardName())
                .boardType(requestDTO.getBoardType())
                .allowAnonymous(requestDTO.isAllowAnonymous())
                .allowComments(requestDTO.isAllowComments())
                .allowEdit(requestDTO.isAllowEdit())
                .allowDelete(requestDTO.isAllowDelete())
                .build();

        board.setCreatedBy(user.getName());  // 작성자 정보

        Board savedBoard = boardRepository.save(board);
        return convertToResponseDTO(savedBoard);
    }

    // 게시판 수정
    @Transactional
    public BoardResponseDTO updateBoard(UUID boardId, BoardRequestDTO requestDTO, UUID userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        UserAuthentication user = userRepository.findById(userId)  // 사용자 정보 조회
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

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
    public void deleteBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        // 게시판 삭제 권한 확인
        if (!board.isAllowDelete()) {
            throw new IllegalStateException("삭제가 허용되지 않은 게시판입니다.");
        }

        // 게시글 수 확인
        long postCount = postRepository.countByBoard(board);
        if (postCount > 0) {
            throw new IllegalStateException("게시글이 존재하는 게시판은 삭제할 수 없습니다.");
        }

        boardRepository.delete(board);
    }

    // 게시판 조회
    public BoardResponseDTO getBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));
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
