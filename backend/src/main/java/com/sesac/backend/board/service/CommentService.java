package com.sesac.backend.board.service;

import com.sesac.backend.board.dto.request.CommentRequestDTO;
import com.sesac.backend.board.dto.response.CommentResponseDTO;
import com.sesac.backend.board.repository.CommentRepository;
import com.sesac.backend.board.repository.PostRepository;
import com.sesac.backend.board.repository.UserAuthenticationRepository;
import com.sesac.backend.entity.Comment;
import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.UserAuthentication;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserAuthenticationRepository userRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO, UUID userId) {
        Post post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserAuthentication author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .content(requestDTO.getContent())
                .anonymous(requestDTO.isAnonymous())  // 변경
                .build();
        comment.setCreatedBy(author.getName());
        Comment savedComment = commentRepository.save(comment);
        return convertToResponseDTO(savedComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDTO updateComment(UUID commentId, CommentRequestDTO requestDTO, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        UserAuthentication user = userRepository.findById(userId)  // 수정자 정보 조회
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        log.info("Before update - anonymous: {}", comment.isAnonymous());
        comment.setContent(requestDTO.getContent());
        comment.setAnonymous(requestDTO.isAnonymous());
        comment.setUpdatedBy(user.getName());
        log.info("After update - anonymous: {}", comment.isAnonymous());

        return convertToResponseDTO(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }

    // 게시글별 댓글 목록 조회
    public List<CommentResponseDTO> getCommentsByPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        return commentRepository.findByPostOrderByCreatedAtDesc(post).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private CommentResponseDTO convertToResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .content(comment.getContent())
                .authorName(comment.isAnonymous() ? "익명" : comment.getAuthor().getName())
                .anonymous(comment.isAnonymous())
                .createdAt(comment.getCreatedAt())
                .createdBy(comment.getCreatedBy())
                .build();
    }
}
