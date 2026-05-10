package com.example.blog.enums;

import lombok.Getter;

@Getter
public enum CommentStatus {
    PENDING(0, "待审核"),
    APPROVED(1, "通过"),
    REJECTED(2, "拒绝");

    private final int code;
    private final String desc;

    CommentStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommentStatus fromCode(int code) {
        for (CommentStatus status : values()) {
            if (status.code == code) return status;
        }
        throw new IllegalArgumentException("Invalid CommentStatus code: " + code);
    }
}
