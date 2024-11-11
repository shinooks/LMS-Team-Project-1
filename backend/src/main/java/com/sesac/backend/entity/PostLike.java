package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(indexes = @Index(name = "uk_post_like", columnList = "postId,userId", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserAuthentication user;
}
