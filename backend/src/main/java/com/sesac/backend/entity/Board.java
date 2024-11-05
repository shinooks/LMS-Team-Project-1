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
public class Board {

    @Id
    @GeneratedValue
    private UUID boardId; // 게시판ID

    @Column(nullable = false)
    private String boardName; // 게시판이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType; // 게시판유형

    private boolean allowAnonymous = false; // 익명허용
    private boolean allowComments = true; // 댓글허용
    private boolean allowEdit = true; // 수정허용
    private boolean allowDelete = true; // 삭제허용

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일시

    public enum BoardType {
        NOTICE, GENERAL, QNA
    }
}
