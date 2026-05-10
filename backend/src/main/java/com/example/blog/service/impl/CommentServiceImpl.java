package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CommentVO;
import com.example.blog.entity.Article;
import com.example.blog.entity.Comment;
import com.example.blog.entity.CommentStatus;
import com.example.blog.mapper.ArticleMapper;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void submitComment(CommentDTO commentDTO, String ip) {
        if (!StringUtils.hasText(commentDTO.getNickname()) || !StringUtils.hasText(commentDTO.getEmail()) || !StringUtils.hasText(commentDTO.getContent())) {
            throw new RuntimeException("请填写完整的评论信息");
        }
        Article article = articleMapper.selectById(commentDTO.getArticleId());
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        Comment comment = new Comment();
        comment.setArticleId(commentDTO.getArticleId());
        comment.setNickname(commentDTO.getNickname());
        comment.setEmail(commentDTO.getEmail());
        comment.setContent(commentDTO.getContent());
        comment.setIp(ip);
        comment.setStatus(CommentStatus.PENDING.getCode());
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        this.save(comment);
    }

    @Override
    public void approveComment(Long id) {
        Comment comment = this.getById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        comment.setStatus(CommentStatus.APPROVED.getCode());
        comment.setUpdateTime(LocalDateTime.now());
        this.updateById(comment);
    }

    @Override
    public void rejectComment(Long id) {
        Comment comment = this.getById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        comment.setStatus(CommentStatus.REJECTED.getCode());
        comment.setUpdateTime(LocalDateTime.now());
        this.updateById(comment);
    }

    @Override
    public List<CommentVO> getApprovedComments(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getStatus, CommentStatus.APPROVED.getCode());
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = this.list(queryWrapper);
        return comments.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Page<CommentVO> getCommentPage(Page<Comment> page, Integer status, Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            queryWrapper.eq(Comment::getStatus, status);
        }
        if (articleId != null) {
            queryWrapper.eq(Comment::getArticleId, articleId);
        }
        queryWrapper.orderByDesc(Comment::getCreateTime);
        this.page(page, queryWrapper);
        Page<CommentVO> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage);
        List<CommentVO> vos = page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(vos);
        return voPage;
    }

    @Override
    public Long getApprovedCommentCount(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getStatus, CommentStatus.APPROVED.getCode());
        return this.count(queryWrapper);
    }

    private CommentVO convertToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);
        Article article = articleMapper.selectById(comment.getArticleId());
        if (article != null) {
            vo.setArticleTitle(article.getTitle());
        }
        return vo;
    }
}
