package com.sesac.backend.board.service;

import com.sesac.backend.board.dto.request.PostFileRequestDTO;
import com.sesac.backend.board.dto.response.PostFileResponseDTO;
import com.sesac.backend.board.repository.PostFileRepository;
import com.sesac.backend.board.repository.PostRepository;
import com.sesac.backend.entity.Post;
import com.sesac.backend.entity.PostFile;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    @Transactional(readOnly = true)
    public class PostFileService {

        private final PostFileRepository postFileRepository;
        private final PostRepository postRepository;

        // 파일 업로드
        @Transactional
        public PostFileResponseDTO uploadFile(PostFileRequestDTO requestDTO, byte[] fileData) {
            Post post = postRepository.findById(requestDTO.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

            String storedName = generateStoredName(requestDTO.getOriginalName());
            String fileType = determineFileType(requestDTO.getOriginalName());
            long fileSize = fileData.length;

            PostFile postFile = PostFile.builder()
                    .post(post)
                    .build();

            postFile.setOriginalName(requestDTO.getOriginalName());
            postFile.setStoredName(storedName);
            postFile.setFilePath("/uploads/" + storedName);
            postFile.setFileData(fileData);
            postFile.setFileSize(fileSize);
            postFile.setFileType(fileType);

            PostFile savedFile = postFileRepository.save(postFile);
            return convertToResponseDTO(savedFile);
        }

        // 파일 다운로드
        public byte[] downloadFile(UUID fileId) {
            PostFile postFile = postFileRepository.findById(fileId)
                    .orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다."));
            return postFile.getFileData();
        }

        // 게시글의 첨부파일 목록 조회
        public List<PostFileResponseDTO> getFilesByPost(UUID postId) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

            return postFileRepository.findByPost(post).stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        }

        // 파일 정보 조회
        public PostFileResponseDTO getFileInfo(UUID fileId) {
            PostFile postFile = postFileRepository.findById(fileId)
                    .orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다."));
            return convertToResponseDTO(postFile);
        }

        // 파일 삭제
        @Transactional
        public void deleteFile(UUID fileId) {
            PostFile postFile = postFileRepository.findById(fileId)
                    .orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다."));
            postFileRepository.delete(postFile);
        }

        // 저장될 파일명 생성
        private String generateStoredName(String originalName) {
            return UUID.randomUUID().toString() + getFileExtension(originalName);
        }

        // 파일 확장자 추출
        private String getFileExtension(String fileName) {
            int lastDotIndex = fileName.lastIndexOf(".");
            return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex);
        }

        // 파일 타입 결정
        private String determineFileType(String fileName) {
            String extension = getFileExtension(fileName).toLowerCase();
            return switch (extension) {
                case ".pdf" -> "application/pdf";
                case ".jpg", ".jpeg" -> "image/jpeg";
                case ".png" -> "image/png";
                case ".gif" -> "image/gif";
                case ".doc", ".docx" -> "application/msword";
                case ".xls", ".xlsx" -> "application/vnd.ms-excel";
                case ".ppt", ".pptx" -> "application/vnd.ms-powerpoint";
                case ".txt" -> "text/plain";
                case ".zip" -> "application/zip";
                default -> "application/octet-stream";
            };
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
                    .build();
        }
    }