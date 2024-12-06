package com.sesac.backend.board.repository;

import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, UUID> {
    // 특정 게시글의 첨부파일 목록 조회
    List<PostFile> findByPost(Post post);

    // 삭제된 파일 조회 메서드 추가
    @Query("SELECT pf FROM PostFile pf WHERE pf.isDeleted = true AND pf.deletedAt <= :threshold")
    List<PostFile> findByIsDeletedTrueAndDeletedAtBefore(@Param("threshold") LocalDateTime threshold);


}