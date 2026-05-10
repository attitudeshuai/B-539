package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CommentVO;
import com.example.blog.entity.Comment;
import com.example.blog.enums.CommentStatus;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public void submitComment(CommentDTO commentDTO, String ip) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setStatus(CommentStatus.PENDING.getCode());
        comment.setIp(ip);
        comment.setCreateTime(LocalDateTime.now());
        this.save(comment);
    }

    @Override
    public Page<Comment> getCommentPage(Integer current, Integer size, Integer status) {
        Page<Comment> page = new Page<>(current, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Comment::getStatus, status);
        }
        wrapper.orderByDesc(Comment::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void approveComment(Long id) {
        Comment comment = this.getById(id);
        if (comment == null) throw new RuntimeException("评论不存在");
        comment.setStatus(CommentStatus.APPROVED.getCode());
        this.updateById(comment);
    }

    @Override
    public void rejectComment(Long id) {
        Comment comment = this.getById(id);
        if (comment == null) throw new RuntimeException("评论不存在");
        comment.setStatus(CommentStatus.REJECTED.getCode());
        this.updateById(comment);
    }

    @Override
    public List<CommentVO> getApprovedComments(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
               .eq(Comment::getStatus, CommentStatus.APPROVED.getCode())
               .orderByDesc(Comment::getCreateTime);
        return this.list(wrapper).stream().map(c -> {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Long getApprovedCount(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
               .eq(Comment::getStatus, CommentStatus.APPROVED.getCode());
        return this.count(wrapper);
    }
}
