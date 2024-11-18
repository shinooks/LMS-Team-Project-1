package com.sesac.backend.board.repository;
import com.sesac.backend.entity.Board;
import com.sesac.backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {
    // JpaSpecificationExecutor를 통해 모든 검색 기능 구현
    // 게시판별 게시글 수 조회 메서드 추가
    long countByBoard(Board board);
}