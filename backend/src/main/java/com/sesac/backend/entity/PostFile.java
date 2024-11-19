package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFile extends BaseFile {
    @Id
    @GeneratedValue
    private UUID fileId;  // 파일ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;    // 게시글ID

    private boolean isDeleted;         // 삭제 여부
    private LocalDateTime deletedAt;   // 삭제 일시
}
