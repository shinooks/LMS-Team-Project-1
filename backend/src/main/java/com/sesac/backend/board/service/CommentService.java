package com.sesac.backend.board.service;

import com.sesac.backend.board.constant.BoardConstants;
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
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Comment.ERROR_POST_NOT_FOUND));

        UserAuthentication author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Comment.ERROR_USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .content(requestDTO.getContent())
                .anonymous(requestDTO.isAnonymous())
                .build();
        comment.setCreatedBy(author.getName());
        Comment savedComment = commentRepository.save(comment);
        return convertToResponseDTO(savedComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDTO updateComment(UUID commentId, CommentRequestDTO requestDTO, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Comment.ERROR_NOT_FOUND));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Comment.ERROR_USER_NOT_FOUND));

        log.info(BoardConstants.Comment.LOG_BEFORE_UPDATE, comment.isAnonymous());
        comment.setContent(requestDTO.getContent());
        comment.setAnonymous(requestDTO.isAnonymous());
        comment.setUpdatedBy(user.getName());
        log.info(BoardConstants.Comment.LOG_AFTER_UPDATE, comment.isAnonymous());

        return convertToResponseDTO(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Comment.ERROR_NOT_FOUND));
        commentRepository.delete(comment);
    }

    // 게시글별 댓글 목록 조회
    public List<CommentResponseDTO> getCommentsByPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Comment.ERROR_POST_NOT_FOUND));

        return commentRepository.findByPostOrderByCreatedAtDesc(post).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private CommentResponseDTO convertToResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .content(comment.getContent())
                .authorName(comment.isAnonymous() ? BoardConstants.Comment.ANONYMOUS_USER : comment.getAuthor().getName())
                .anonymous(comment.isAnonymous())
                .createdAt(comment.getCreatedAt())
                .createdBy(comment.getCreatedBy())
                .build();
    }
}
