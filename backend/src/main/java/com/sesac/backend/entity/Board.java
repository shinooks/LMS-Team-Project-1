package com.sesac.backend.entity;

import com.sesac.backend.board.constant.BoardType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID boardId; // 게시판ID

    @Column(nullable = false)
    private String boardName; // 게시판이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType; // 게시판유형 (공지사항, 일반, Q&A)

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean allowAnonymous; // 익명허용

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean allowComments; // 댓글허용

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean allowEdit; // 수정허용

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean allowDelete; // 삭제허용

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) // 게시판 삭제시 연관된 게시글도 함께 삭제
    private List<Post> posts = new ArrayList<>();
}
