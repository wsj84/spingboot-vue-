package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.*;
import com.wang.animeplatform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /* 查询所有评论 */
    @GetMapping("/all")
    public Result findAll() {
        List<Comment> comments = commentService.findAll();
        return Result.success(comments);
    }
    @GetMapping("/query")
    public Result queryComment(PageAll requestPage) {
        PageResult pageResult = commentService.queryComment(requestPage);
        return Result.success(pageResult);
    }

    /* 根据ID查询评论 */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        Comment comment = commentService.findById(id);
        return Result.success(comment);
    }

    /* 添加评论 */
    @PostMapping
    public Result addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
        return Result.success();
    }

    /* 更新评论 */
    @PutMapping
    public Result updateComment(@RequestBody Comment comment) {
        commentService.updateComment(comment);
        return Result.success();
    }

    /* 删除评论 */
    @DeleteMapping("/{id}")
    public Result deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return Result.success();
    }
}
