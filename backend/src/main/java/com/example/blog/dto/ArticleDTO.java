package com.example.blog.dto;

import lombok.Data;
import java.util.List;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private List<Long> tagIds;
    private Integer status;
}
