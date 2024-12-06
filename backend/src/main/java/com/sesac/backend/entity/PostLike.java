package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(indexes = @Index(name = "uk_post_like", columnList = "post_id,author_id", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)  // userId -> author_id로 변경
    private UserAuthentication user;  // 필드명은 user로 유지
}
