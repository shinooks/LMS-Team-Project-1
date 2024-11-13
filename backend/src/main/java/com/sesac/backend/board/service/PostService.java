package com.sesac.backend.board.service;
import com.sesac.backend.board.constant.UserType;
import com.sesac.backend.board.dto.request.PostRequestDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserAuthenticationRepository userRepository;

    // 게시글 작성
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO requestDTO, UUID userId) {
        Board board = boardRepository.findById(requestDTO.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        UserAuthentication author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .board(board)
                .author(author)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .isAnonymous(requestDTO.isAnonymous())
                .viewCount(0)
                .build();

        post.setCreatedBy(author.getName());

        Post savedPost = postRepository.save(post);
        return convertToResponseDTO(savedPost);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDTO updatePost(UUID postId, PostRequestDTO requestDTO, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 권한 체크
        if (!hasPermissionToModify(post, user)) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }

        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());
        post.setAnonymous(requestDTO.isAnonymous());
        post.setUpdatedBy(user.getName());  // 수정자 정보 업데이트

        return convertToResponseDTO(post);
    }

    // 게시판별 게시글 수 조회
    public long getPostCount(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));
        return postRepository.countByBoard(board);
    }

    // 게시글 조회 (조회수 증가)
    @Transactional
    public PostResponseDTO getPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.setViewCount(post.getViewCount() + 1);
        return convertToResponseDTO(post);
    }

    // 게시판별 게시글 목록 조회
    public List<PostResponseDTO> getPostsByBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        return postRepository.findByBoardOrderByCreatedAtDesc(board).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 게시글 검색 (제목)
    public List<PostResponseDTO> searchPostsByTitle(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 게시글 검색 (내용)
    public List<PostResponseDTO> searchPostsByContent(String keyword) {
        return postRepository.findByContentContaining(keyword).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 작성자별 게시글 목록 조회
    public List<PostResponseDTO> getPostsByAuthor(UUID userId) {
        UserAuthentication author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return postRepository.findByAuthorOrderByCreatedAtDesc(author).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 특정 기간 내의 게시글 조회
    public List<PostResponseDTO> getPostsByDateRange(LocalDateTime start, LocalDateTime end) {
        return postRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 게시판의 삭제 허용 여부 확인
        if (!post.getBoard().isAllowDelete()) {
            throw new IllegalStateException("이 게시판은 게시글 삭제가 허용되지 않습니다.");
        }

        // 권한 체크
        if (!hasPermissionToModify(post, user)) {
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // 게시글 수정/삭제 권한 체크 메서드
    private boolean hasPermissionToModify(Post post, UserAuthentication user) {
        // 교직원은 모든 게시글에 대한 권한을 가짐
        if (user.getUserType() == UserType.STAFF) {
            return true;
        }

        // 그 외의 경우 본인이 작성한 게시글만 수정/삭제 가능
        return post.getAuthor().getUserId().equals(user.getUserId());
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

                // 좋아요 목록 변환
                .likes(post.getLikes().stream()
                        .map(like -> PostLikeResponseDTO.builder()
                                .likeId(like.getLikeId())
                                .postId(like.getPost().getPostId())
                                .userName(like.getUser().getName())
                                .createdAt(like.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))

                // 댓글 목록 변환
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

                // 첨부파일 목록 변환
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