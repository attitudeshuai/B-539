package com.example.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.annotation.RateLimit;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CommentVO;
import com.example.blog.entity.Comment;
import com.example.blog.service.CommentService;
import com.example.blog.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RateLimit(maxRequests = 5, windowSeconds = 60)
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        commentService.submitComment(commentDTO, ip);
        return Result.success("评论提交成功，等待审核");
    }

    @GetMapping("/article/{articleId}")
    public Result<List<CommentVO>> getArticleComments(@PathVariable Long articleId) {
        return Result.success(commentService.getApprovedComments(articleId));
    }

    @GetMapping("/page")
    public Result<Page<CommentVO>> getPage(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) Integer status,
                                           @RequestParam(required = false) Long articleId) {
        Page<Comment> page = new Page<>(current, size);
        return Result.success(commentService.getCommentPage(page, status, articleId));
    }

    @PutMapping("/approve/{id}")
    public Result<String> approve(@PathVariable Long id) {
        commentService.approveComment(id);
        return Result.success("评论已通过");
    }

    @PutMapping("/reject/{id}")
    public Result<String> reject(@PathVariable Long id) {
        commentService.rejectComment(id);
        return Result.success("评论已拒绝");
    }
}
