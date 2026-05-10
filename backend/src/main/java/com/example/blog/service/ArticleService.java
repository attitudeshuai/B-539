package com.example.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.dto.ArticleDTO;
import com.example.blog.dto.ArticleVO;
import com.example.blog.entity.Article;

public interface ArticleService extends IService<Article> {
    void saveArticle(ArticleDTO articleDTO);
    void updateArticle(ArticleDTO articleDTO);
    ArticleVO getArticleDetail(Long id);
    Page<ArticleVO> getArticlePage(Page<Article> page, String keyword, Long categoryId, Long tagId);
}
