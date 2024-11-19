package com.sesac.backend.board.service;

import com.sesac.backend.board.constant.BoardConstants;
import com.sesac.backend.board.constant.BoardType;
import com.sesac.backend.board.constant.UserType;
import com.sesac.backend.board.dto.request.PostRequestDTO;
import com.sesac.backend.board.dto.request.PostSearchRequestDto;
import com.sesac.backend.board.dto.response.CommentResponseDTO;
import com.sesac.backend.board.dto.response.PostFileResponseDTO;
import com.sesac.backend.board.dto.response.PostLikeResponseDTO;
import com.sesac.backend.board.dto.response.PostResponseDTO;
import com.sesac.backend.board.repository.BoardRepository;
import com.sesac.backend.board.repository.PostRepository;
import com.sesac.backend.board.repository.UserAuthenticationRepository;
import com.sesac.backend.entity.Board;
import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.UserAuthentication;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;            // 게시글 레포지토리
    private final BoardRepository boardRepository;         // 게시판 레포지토리
    private final UserAuthenticationRepository userRepository;  // 사용자 레포지토리

    // 게시글 작성
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO requestDTO, UUID userId) {
        Board board = boardRepository.findById(requestDTO.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Post.ERROR_BOARD_NOT_FOUND));

        UserAuthentication author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        Post post = Post.builder()
                .board(board)
                .author(author)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .isAnonymous(requestDTO.isAnonymous())
                .viewCount(0)
                .build();

        post.setCreatedBy(author.getName());
        log.info(BoardConstants.Post.LOG_CREATE, requestDTO);
        Post savedPost = postRepository.save(post);
        return convertToResponseDTO(savedPost);
    }

    // 게시글 조회 (조회수 증가)
    @Transactional
    public PostResponseDTO getPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Post.ERROR_NOT_FOUND));

        post.setViewCount(post.getViewCount() + 1);
        log.info(BoardConstants.Post.LOG_GET, postId);
        return convertToResponseDTO(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDTO updatePost(UUID postId, PostRequestDTO requestDTO, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Post.ERROR_NOT_FOUND));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        if (!hasPermissionToModify(post, user)) {
            throw new IllegalStateException(BoardConstants.Post.ERROR_NO_PERMISSION_MODIFY);
        }

        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());
        post.setAnonymous(requestDTO.isAnonymous());
        post.setUpdatedBy(user.getName());

        log.info(BoardConstants.Post.LOG_UPDATE, postId);
        return convertToResponseDTO(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Post.ERROR_NOT_FOUND));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        if (!hasPermissionToModify(post, user)) {
            throw new IllegalStateException(BoardConstants.Post.ERROR_NO_PERMISSION_DELETE);
        }

        log.info(BoardConstants.Post.LOG_DELETE, postId);
        postRepository.delete(post);
    }

    // 게시글 검색 (페이징)
    public Page<PostResponseDTO> searchPosts(
            UUID boardId,
            PostSearchRequestDto searchDto,
            Pageable pageable) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Post.ERROR_BOARD_NOT_FOUND));

        log.info(BoardConstants.Post.LOG_SEARCH, searchDto);

        Specification<Post> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("board"), board));

            if (searchDto.getBoardType() != null) {
                predicates.add(cb.equal(root.get("board").get("boardType"),
                        searchDto.getBoardType()));
            }

            if (searchDto.getAuthorId() != null) {
                predicates.add(cb.equal(root.get("author").get("userId"),
                        searchDto.getAuthorId()));
            }

            if (!searchDto.getKeyword().isEmpty()) {
                if ("content".equalsIgnoreCase(searchDto.getSearchType())) {
                    predicates.add(cb.like(cb.lower(root.get("content")),
                            "%" + searchDto.getKeyword().toLowerCase() + "%"));
                } else {
                    predicates.add(cb.like(cb.lower(root.get("title")),
                            "%" + searchDto.getKeyword().toLowerCase() + "%"));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return postRepository.findAll(spec, pageable).map(this::convertToResponseDTO);
    }

    // 게시글 수정/삭제 권한 확인
    private boolean hasPermissionToModify(Post post, UserAuthentication user) {
        return user.getUserType() == UserType.STAFF ||
                post.getAuthor().getUserId().equals(user.getUserId());
    }

    // Entity -> DTO 변환
    private PostResponseDTO convertToResponseDTO(Post post) {
        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .boardId(post.getBoard().getBoardId())
                .boardName(post.getBoard().getBoardName())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.isAnonymous() ? "익명" : post.getAuthor().getName())
                .isAnonymous(post.isAnonymous())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikes().size())
                .likes(post.getLikes().stream()
                        .map(like -> PostLikeResponseDTO.builder()
                                .likeId(like.getLikeId())
                                .postId(like.getPost().getPostId())
                                .userName(like.getUser().getName())
                                .createdAt(like.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .comments(post.getComments().stream()
                        .map(comment -> CommentResponseDTO.builder()
                                .commentId(comment.getCommentId())
                                .postId(comment.getPost().getPostId())
                                .content(comment.getContent())
                                .authorName(comment.isAnonymous() ? "익명" : comment.getAuthor().getName())
                                .anonymous(comment.isAnonymous())
                                .createdAt(comment.getCreatedAt())
                                .createdBy(comment.getCreatedBy())
                                .build())
                        .collect(Collectors.toList()))
                .files(post.getFiles().stream()
                        .map(file -> PostFileResponseDTO.builder()
                                .fileId(file.getFileId())
                                .postId(file.getPost().getPostId())
                                .originalName(file.getOriginalName())
                                .storedName(file.getStoredName())
                                .filePath(file.getFilePath())
                                .fileSize(file.getFileSize())
                                .fileType(file.getFileType())
                                .createdAt(file.getCreatedAt())
                                .createdBy(file.getCreatedBy())
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .createdBy(post.getCreatedBy())
                .build();
    }
}