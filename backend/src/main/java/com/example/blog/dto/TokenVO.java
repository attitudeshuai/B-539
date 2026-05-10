package com.example.blog.dto;

import lombok.Data;

@Data
public class TokenVO {
    private String token;
    private Long id;
    private String username;
    private String nickname;
    private Integer role;
}
