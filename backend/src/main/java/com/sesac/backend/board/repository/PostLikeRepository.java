package com.sesac.backend.board.repository;

import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.PostLike;
import com.sesac.backend.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    // 특정 게시글의 좋아요 여부 확인
    boolean existsByPostAndUser(Post post, UserAuthentication user);

    // 특정 게시글의 좋아요 삭제
    void deleteByPostAndUser(Post post, UserAuthentication user);

    // 특정 게시글의 좋아요 수 조회
    long countByPost(Post post);

}
