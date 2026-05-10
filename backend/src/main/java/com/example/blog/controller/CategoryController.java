package com.example.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.entity.Category;
import com.example.blog.service.CategoryService;
import com.example.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.list());
    }

    @GetMapping("/page")
    public Result<Page<Category>> page(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(categoryService.page(new Page<>(current, size)));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Category category) {
        category.setCreateTime(LocalDateTime.now());
        categoryService.save(category);
        return Result.success("保存成功");
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success("删除成功");
    }
}
