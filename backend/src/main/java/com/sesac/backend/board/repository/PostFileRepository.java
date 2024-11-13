package com.sesac.backend.board.repository;

import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, UUID> {
    // 특정 게시글의 첨부파일 목록 조회
    List<PostFile> findByPost(Post post);
}