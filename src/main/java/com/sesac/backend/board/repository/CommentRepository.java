package com.sesac.backend.board.repository;

import com.sesac.backend.entity.Comment;
import com.sesac.backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    // 특정 게시글의 댓글 목록 조회 (최신순)
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}