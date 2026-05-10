package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.ArticleDTO;
import com.example.blog.dto.ArticleVO;
import com.example.blog.entity.*;
import com.example.blog.mapper.*;
import com.example.blog.service.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setViewCount(0);
        this.save(article);
        saveArticleTags(article.getId(), articleDTO.getTagIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleDTO articleDTO) {
        Article article = this.getById(articleDTO.getId());
        if (article == null) throw new RuntimeException("Article not found");
        BeanUtils.copyProperties(articleDTO, article);
        article.setUpdateTime(LocalDateTime.now());
        this.updateById(article);
        
        // delete old tags
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagMapper.delete(queryWrapper);
        
        saveArticleTags(article.getId(), articleDTO.getTagIds());
    }

    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (tagIds != null) {
            for (Long tagId : tagIds) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tagId);
                articleTagMapper.insert(articleTag);
            }
        }
    }

    @Override
    public ArticleVO getArticleDetail(Long id) {
        Article article = this.getById(id);
        if (article == null) return null;
        // update view count
        article.setViewCount(article.getViewCount() + 1);
        this.updateById(article);
        return convertToVO(article);
    }

    @Override
    public Page<ArticleVO> getArticlePage(Page<Article> page, String keyword, Long categoryId, Long tagId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Article::getTitle, keyword);
        }
        if (categoryId != null) {
            queryWrapper.eq(Article::getCategoryId, categoryId);
        }
        if (tagId != null) {
            LambdaQueryWrapper<ArticleTag> atWrapper = new LambdaQueryWrapper<>();
            atWrapper.eq(ArticleTag::getTagId, tagId);
            List<ArticleTag> articleTags = articleTagMapper.selectList(atWrapper);
            if (articleTags.isEmpty()) {
                return new Page<>();
            }
            List<Long> articleIds = articleTags.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());
            queryWrapper.in(Article::getId, articleIds);
        }
        queryWrapper.orderByDesc(Article::getCreateTime);
        
        this.page(page, queryWrapper);
        
        Page<ArticleVO> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<ArticleVO> vos = page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(vos);
        return voPage;
    }

    private ArticleVO convertToVO(Article article) {
        ArticleVO vo = new ArticleVO();
        BeanUtils.copyProperties(article, vo);
        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
        LambdaQueryWrapper<ArticleTag> atWrapper = new LambdaQueryWrapper<>();
        atWrapper.eq(ArticleTag::getArticleId, article.getId());
        List<ArticleTag> articleTags = articleTagMapper.selectList(atWrapper);
        if (!articleTags.isEmpty()) {
            List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
            List<Tag> tags = tagMapper.selectBatchIds(tagIds);
            vo.setTags(tags);
        } else {
            vo.setTags(new ArrayList<>());
        }
        return vo;
    }
}
