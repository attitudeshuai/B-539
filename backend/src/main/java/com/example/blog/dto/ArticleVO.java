package com.example.blog.dto;

import com.example.blog.entity.Tag;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleVO {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private List<Tag> tags;
    private Integer status;
    private Integer viewCount;
    private Long commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
