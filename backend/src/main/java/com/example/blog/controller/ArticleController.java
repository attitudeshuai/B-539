package com.example.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.dto.ArticleDTO;
import com.example.blog.dto.ArticleVO;
import com.example.blog.entity.Article;
import com.example.blog.service.ArticleService;
import com.example.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/page")
    public Result<Page<ArticleVO>> getPage(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Long tagId) {
        Page<Article> page = new Page<>(current, size);
        return Result.success(articleService.getArticlePage(page, keyword, categoryId, tagId));
    }

    @GetMapping("/{id}")
    public Result<ArticleVO> getDetail(@PathVariable Long id) {
        return Result.success(articleService.getArticleDetail(id));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody ArticleDTO articleDTO) {
        articleService.saveArticle(articleDTO);
        return Result.success("保存成功");
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody ArticleDTO articleDTO) {
        articleService.updateArticle(articleDTO);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success("删除成功");
    }
}
