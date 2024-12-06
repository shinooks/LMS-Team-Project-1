package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID commentId; // 댓글ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post; // 게시글ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId", nullable = false)
    private UserAuthentication author; // 작성자ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 내용

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean anonymous; // 익명여부
}
