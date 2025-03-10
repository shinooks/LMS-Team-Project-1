package com.sesac.backend.board.service;

import com.sesac.backend.aws.dto.FileDownloadResponse;
import com.sesac.backend.aws.dto.FileUploadResponse;
import com.sesac.backend.aws.service.S3Service;
import com.sesac.backend.board.constant.BoardConstants;
import com.sesac.backend.board.constant.UserType;
import com.sesac.backend.board.dto.request.PostFileRequestDTO;
import com.sesac.backend.board.dto.response.PostFileResponseDTO;
import com.sesac.backend.board.repository.PostFileRepository;
import com.sesac.backend.board.repository.PostRepository;
import com.sesac.backend.board.repository.UserAuthenticationRepository;
import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.PostFile;
import com.sesac.backend.entity.UserAuthentication;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostFileService {
    private final PostFileRepository postFileRepository;
    private final PostRepository postRepository;
    private final UserAuthenticationRepository userRepository;
    private final S3Service s3Service;

    // 파일 업로드
    @Transactional
    public PostFileResponseDTO uploadFile(PostFileRequestDTO requestDTO, byte[] fileData, UUID userId) {
        try {
            // 파일 검증
            validateFile(fileData, requestDTO.getFileType());

            Post post = postRepository.findById(requestDTO.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException(BoardConstants.PostFile.ERROR_POST_NOT_FOUND));

            UserAuthentication user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

            // requestDTO의 파일 타입을 사용하여 MultipartFile 생성
            MultipartFile multipartFile = new MockMultipartFile(
                    BoardConstants.Common.PARAM_FILE,
                    requestDTO.getOriginalName(),
                    requestDTO.getFileType(),
                    fileData
            );

            // S3에 파일 업로드
            FileUploadResponse s3Response = s3Service.uploadFile(multipartFile, user.getName());

            // DB에 파일 정보 저장
            PostFile postFile = PostFile.builder()
                    .post(post)
                    .fileId(UUID.randomUUID())
                    .build();

            // BaseFile 필드들 설정
            postFile.setOriginalName(requestDTO.getOriginalName());
            postFile.setStoredName(s3Response.getSavedFileName());
            postFile.setFilePath(s3Response.getFileUrl());
            postFile.setFileSize(s3Response.getSize());
            postFile.setFileType(s3Response.getContentType());
            postFile.setDeleted(false);
            postFile.setCreatedBy(user.getName());
            postFile.setUpdatedBy(user.getName());

            PostFile savedFile = postFileRepository.save(postFile);
            return convertToResponseDTO(savedFile);

        } catch (Exception e) {
            log.error(BoardConstants.PostFile.LOG_FILE_UPLOAD_ERROR, e.getMessage());
            throw new RuntimeException(BoardConstants.PostFile.ERROR_FILE_UPLOAD, e);
        }
    }

    // 파일 다운로드 - 캐시 조건 수정
    @Cacheable(value = BoardConstants.PostFile.CACHE_FILE_DOWNLOAD, key = "#fileId",
            unless = "#result == null || #root.target.isFileDeleted(#fileId)")
    public byte[] downloadFile(UUID fileId, UUID userId) {
        if (fileId == null) {
            throw new IllegalArgumentException(BoardConstants.PostFile.ERROR_FILE_ID_REQUIRED);
        }

        PostFile postFile = postFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.PostFile.ERROR_FILE_NOT_FOUND));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        if (postFile.isDeleted()) {
            throw new IllegalStateException(BoardConstants.PostFile.ERROR_FILE_DELETED);
        }

        // S3에서 파일 다운로드
        FileDownloadResponse s3Response = s3Service.downloadFile(postFile.getStoredName(), user.getName());
        return s3Response.getFileContent();
    }

    // 게시글의 첨부파일 목록 조회 - 캐시 키 수정
    @Cacheable(value = BoardConstants.PostFile.CACHE_FILE_LIST, key = "#postId")
    public List<PostFileResponseDTO> getFilesByPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.PostFile.ERROR_POST_NOT_FOUND));

        return postFileRepository.findByPost(post).stream()
                .filter(file -> !file.isDeleted())  // 삭제되지 않은 파일만 반환
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 파일 정보 조회 - 캐시 키 수정
    @Cacheable(value = BoardConstants.PostFile.CACHE_FILE_INFO, key = "#fileId", unless = "#result.isDeleted")
    public PostFileResponseDTO getFileInfo(UUID fileId) {
        PostFile postFile = postFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.PostFile.ERROR_FILE_NOT_FOUND));
        return convertToResponseDTO(postFile);
    }

    // 파일 삭제
    @Transactional
    public void deleteFile(UUID fileId, UUID userId) {
        PostFile postFile = postFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.PostFile.ERROR_FILE_NOT_FOUND));

        UserAuthentication user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(BoardConstants.Common.ERROR_USER_NOT_FOUND));

        // 삭제 권한 검증
        validateDeletePermission(postFile, user);

        // 이미 삭제된 파일인지 확인
        if (postFile.isDeleted()) {
            throw new IllegalStateException(BoardConstants.PostFile.ERROR_ALREADY_DELETED);
        }

        // 소프트 삭제 처리
        postFile.setDeleted(true);
        postFile.setDeletedAt(LocalDateTime.now());
        postFile.setUpdatedBy(user.getName());

        postFileRepository.save(postFile);
        clearFileCache(fileId, postFile.getPost().getPostId());
    }

    // 캐시 삭제 - 캐시 키 수정
    @CacheEvict(value = {BoardConstants.PostFile.CACHE_FILE_INFO, BoardConstants.PostFile.CACHE_FILE_LIST,
            BoardConstants.PostFile.CACHE_FILE_DOWNLOAD}, allEntries = true)
    public void clearFileCache(UUID fileId, UUID postId) {
        // 캐시 삭제는 어노테이션으로 처리되므로 메서드 본문은 비워둡니다.
    }

    // 실제 물리적 삭제를 위한 스케줄러 - 로그 메시지 한글화
    @Scheduled(cron = BoardConstants.PostFile.SCHEDULER_DELETE_CRON)
    @Transactional
    public void permanentlyDeleteFiles() {
        log.info("파일 삭제 스케줄러 실행 시작: {}", LocalDateTime.now());

        // 분 단위로 설정된 상수 사용
        LocalDateTime threshold = LocalDateTime.now()
                .minusMinutes(BoardConstants.PostFile.DELETE_AFTER_MINUTES);
        log.info("삭제 기준 시간: {}", threshold);

        List<PostFile> filesToDelete = postFileRepository.findByIsDeletedTrueAndDeletedAtBefore(threshold);
        log.info("삭제 대상 파일 수: {}", filesToDelete.size());

        for (PostFile file : filesToDelete) {
            try {
                log.info("파일 삭제 시도 - fileId: {}, storedName: {}, deletedAt: {}",
                        file.getFileId(), file.getStoredName(), file.getDeletedAt());

                boolean s3DeleteResult = s3Service.deleteFile(file.getStoredName(), file.getUpdatedBy());

                if (s3DeleteResult) {
                    postFileRepository.delete(file);
                    log.info("파일 완전 삭제 완료 - fileId: {}, storedName: {}",
                            file.getFileId(), file.getStoredName());
                } else {
                    log.error("S3 파일 삭제 실패 - fileId: {}, storedName: {}",
                            file.getFileId(), file.getStoredName());
                }
            } catch (Exception e) {
                log.error("파일 삭제 중 예외 발생 - fileId: {}, error: {}",
                        file.getFileId(), e.getMessage(), e);
            }
        }
    }

    // 파일 삭제 여부 확인을 위한 헬퍼 메서드
    public boolean isFileDeleted(UUID fileId) {
        return postFileRepository.findById(fileId)
                .map(PostFile::isDeleted)
                .orElse(true);  // 파일이 없으면 삭제된 것으로 간주
    }

    // 파일 검증
    private void validateFile(byte[] fileData, String fileType) {
        if (fileData.length > BoardConstants.PostFile.MAX_FILE_SIZE) {
            throw new IllegalArgumentException(BoardConstants.PostFile.ERROR_FILE_SIZE);
        }

        if (!isAllowedFileType(fileType)) {
            throw new IllegalArgumentException(BoardConstants.PostFile.ERROR_FILE_TYPE);
        }
    }

    // 허용된 파일 타입 체크
    private boolean isAllowedFileType(String contentType) {
        return Arrays.stream(BoardConstants.PostFile.ALLOWED_FILE_TYPES)
                .anyMatch(contentType::startsWith);
    }

    // 삭제 권한 검증
    private void validateDeletePermission(PostFile postFile, UserAuthentication user) {
        boolean isFileOwner = postFile.getCreatedBy().equals(user.getName());
        boolean isPostOwner = postFile.getPost().getCreatedBy().equals(user.getName());
        boolean isStaff = user.getUserType() == UserType.STAFF;

        if (!isFileOwner && !isPostOwner && !isStaff) {
            throw new IllegalStateException(BoardConstants.Common.ERROR_NO_PERMISSION);
        }
    }

    // Entity -> DTO 변환
    private PostFileResponseDTO convertToResponseDTO(PostFile postFile) {
        return PostFileResponseDTO.builder()
                .fileId(postFile.getFileId())
                .postId(postFile.getPost().getPostId())
                .originalName(postFile.getOriginalName())
                .storedName(postFile.getStoredName())
                .filePath(postFile.getFilePath())
                .fileSize(postFile.getFileSize())
                .fileType(postFile.getFileType())
                .createdAt(postFile.getCreatedAt())
                .updatedAt(postFile.getUpdatedAt())
                .createdBy(postFile.getCreatedBy())
                .updatedBy(postFile.getUpdatedBy())
                .isDeleted(postFile.isDeleted())
                .deletedAt(postFile.getDeletedAt())
                .build();
    }
}