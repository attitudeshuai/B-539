package com.example.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CommentVO;
import com.example.blog.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {
    void submitComment(CommentDTO commentDTO, String ip);
    Page<Comment> getCommentPage(Integer current, Integer size, Integer status);
    void approveComment(Long id);
    void rejectComment(Long id);
    List<CommentVO> getApprovedComments(Long articleId);
    Long getApprovedCount(Long articleId);
}
