package com.sesac.backend.board.repository;

import com.sesac.backend.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
    // 게시판 이름 존재 여부 확인
    boolean existsByBoardName(String boardName);
}