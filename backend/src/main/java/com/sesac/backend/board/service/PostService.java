package com.sesac.backend.board.service;
import com.sesac.backend.board.dto.request.PostRequestDTO;
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
    public PostResponseDTO createPost(PostRequestDTO requestDTO, UUID userId) {  // String -> UUID
        Board board = boardRepository.findById(requestDTO.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        UserAuthentication author = userRepository.findById(userId)  // UUID 타입으로 조회
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .board(board)
                .author(author)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .isAnonymous(requestDTO.isAnonymous())
                .viewCount(0)
                .build();

        Post savedPost = postRepository.save(post);
        return convertToResponseDTO(savedPost);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDTO updatePost(UUID postId, PostRequestDTO requestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());
        post.setAnonymous(requestDTO.isAnonymous());

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
    public void deletePost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 게시판의 삭제 허용 여부 확인
        if (!post.getBoard().isAllowDelete()) {
            throw new IllegalStateException("이 게시판은 게시글 삭제가 허용되지 않습니다.");
        }

        // 게시글 삭제
        postRepository.delete(post);
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
                .createdAt(post.getCreatedAt())
                .createdBy(post.getCreatedBy())
                .build();
    }
}