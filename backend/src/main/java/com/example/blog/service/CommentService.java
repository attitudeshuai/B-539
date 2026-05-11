package com.example.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CommentVO;
import com.example.blog.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {
    void addComment(CommentDTO commentDTO, String ip);
    List<CommentVO> getApprovedComments(Long articleId);
    Page<CommentVO> getCommentPage(Page<Comment> page, Integer status);
    void approveComment(Long id);
    void rejectComment(Long id);
    int getApprovedCommentCount(Long articleId);
}
