package com.sesac.backend.board.constant;

public enum BoardType {
    NOTICE("공지사항"),
    GENERAL("일반게시판"),
    QNA("질문과답변");

    private final String description;    // 게시판 유형 설명

    BoardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}