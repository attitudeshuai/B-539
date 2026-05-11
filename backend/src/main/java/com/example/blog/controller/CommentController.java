package com.example.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CommentVO;
import com.example.blog.entity.Comment;
import com.example.blog.service.CommentService;
import com.example.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public Result<String> addComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        String ip = getClientIp(request);
        commentService.addComment(commentDTO, ip);
        return Result.success("评论提交成功，等待审核");
    }

    @GetMapping("/article/{articleId}")
    public Result<List<CommentVO>> getApprovedComments(@PathVariable Long articleId) {
        return Result.success(commentService.getApprovedComments(articleId));
    }

    @GetMapping("/page")
    public Result<Page<CommentVO>> getPage(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) Integer status) {
        Page<Comment> page = new Page<>(current, size);
        return Result.success(commentService.getCommentPage(page, status));
    }

    @PutMapping("/approve/{id}")
    public Result<String> approve(@PathVariable Long id) {
        commentService.approveComment(id);
        return Result.success("审核通过");
    }

    @PutMapping("/reject/{id}")
    public Result<String> reject(@PathVariable Long id) {
        commentService.rejectComment(id);
        return Result.success("已拒绝");
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
