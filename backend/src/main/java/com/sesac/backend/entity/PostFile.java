package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFile extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID fileId;  // 파일ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;    // 게시글ID

    @Column(nullable = false)
    private String originalName;    // 원본 파일명

    @Column(nullable = false)
    private String storedName;      // 저장된 파일명

    @Lob    // Large Object 타입으로 지정
    @Column(columnDefinition = "oid")  // PostgreSQL specific
    private byte[] fileData;        // 실제 파일 데이터

    private Long fileSize;          // 파일 크기

    private String fileType;        // 파일 타입
}
