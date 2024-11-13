package com.sesac.backend.board.repository;

import com.sesac.backend.entity.Board;
import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    // 특정 게시판의 게시글 목록 조회 (최신순)
    List<Post> findByBoardOrderByCreatedAtDesc(Board board);

    // 제목으로 게시글 검색
    List<Post> findByTitleContaining(String keyword);

    // 내용으로 게시글 검색
    List<Post> findByContentContaining(String keyword);

    // 특정 작성자의 게시글 목록 조회
    List<Post> findByAuthorOrderByCreatedAtDesc(UserAuthentication author);

    // 특정 기간 내의 게시글 조회
    List<Post> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    // 특정 게시판의 게시글 수 조회
    long countByBoard(Board board);
}