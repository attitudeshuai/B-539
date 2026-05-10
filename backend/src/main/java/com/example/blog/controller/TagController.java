package com.example.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.entity.Tag;
import com.example.blog.service.TagService;
import com.example.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public Result<List<Tag>> list() {
        return Result.success(tagService.list());
    }

    @GetMapping("/page")
    public Result<Page<Tag>> page(@RequestParam(defaultValue = "1") Integer current,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(tagService.page(new Page<>(current, size)));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Tag tag) {
        tag.setCreateTime(LocalDateTime.now());
        tagService.save(tag);
        return Result.success("保存成功");
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody Tag tag) {
        tagService.updateById(tag);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.success("删除成功");
    }
}
