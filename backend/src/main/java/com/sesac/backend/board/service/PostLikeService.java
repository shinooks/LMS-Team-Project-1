package com.sesac.backend.board.service;

import com.sesac.backend.board.dto.response.PostLikeResponseDTO;
import com.sesac.backend.board.repository.PostLikeRepository;
import com.sesac.backend.board.repository.PostRepository;
import com.sesac.backend.board.repository.UserAuthenticationRepository;
import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.PostLike;
import com.sesac.backend.entity.UserAuthentication;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserAuthenticationRepository userRepository;

    // 좋아요 토글 (없으면 생성, 있으면 삭제)
    @Transactional
    public PostLikeResponseDTO toggleLike(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            return null;
        } else {
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();

            // 직접 작성자 정보 설정
            postLike.setCreatedBy(user.getName());

            PostLike savedLike = postLikeRepository.save(postLike);
            return convertToResponseDTO(savedLike);
        }
    }

    // 게시글의 좋아요 수 조회
    public long getLikeCount(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        return postLikeRepository.countByPost(post);
    }

    // 사용자가 좋아요 했는지 확인
    public boolean hasUserLiked(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return postLikeRepository.existsByPostAndUser(post, user);
    }

    // Entity -> DTO 변환
    private PostLikeResponseDTO convertToResponseDTO(PostLike postLike) {
        return PostLikeResponseDTO.builder()
                .likeId(postLike.getLikeId())
                .postId(postLike.getPost().getPostId())
                .userName(postLike.getUser().getName())
                .createdAt(postLike.getCreatedAt())
                .build();
    }
}
