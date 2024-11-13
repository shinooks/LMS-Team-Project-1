package com.sesac.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public abstract class BaseFile extends BaseEntity {

    @Column(nullable = false)
    private String originalName;        // 원본 파일명

    @Column(nullable = false)
    private String storedName;          // 저장된 파일명 (UUID)

    @Column
    private String filePath;            // 파일 경로

    @Column(nullable = false)
    private Long fileSize;              // 파일 크기

    @Column(nullable = false)
    private String fileType;            // MIME 타입

    @Column(columnDefinition = "bytea")
    private byte[] fileData;           // 실제 파일 데이터
}
