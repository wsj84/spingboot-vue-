package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.PostComment;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.service.PostCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/post-comments")
public class PostCommentController {
    @Autowired
    private PostCommentService postCommentService;

    /**
     * 获取帖子评论
     */
    @GetMapping
    public Result getComments(
            @RequestParam Integer postId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResult pageResult = postCommentService.findByPostId(postId, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 添加评论
     */
    @PostMapping
    public Result addComment(@RequestBody PostComment comment) {
        // 验证必要字段
        if (comment.getPostId() == null) {
            return Result.error("帖子ID不能为空");
        }
        if (comment.getUserId() == null) {
            return Result.error("用户ID不能为空");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }

        // 设置默认值
        comment.setCreatedAt(new Date());
        comment.setIsDeleted(0);
        comment.setLikeCount(0);
        postCommentService.addComment(comment);
        log.info("comment: {}", comment);
        return Result.success(comment);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result deleteComment(@PathVariable Integer id) {
        postCommentService.deleteComment(id);
        return Result.success();
    }

    /**
     * 获取用户的评论
     */
    @GetMapping("/user/{userId}")
    public Result getUserComments(@PathVariable Integer userId) {
        return Result.success(postCommentService.findByUserId(userId));
    }

    /**
     * 回复评论
     */
    @PostMapping("/{commentId}/replies")
    public Result replyComment(
            @PathVariable Integer commentId,
            @RequestBody PostComment comment) {
        // 设置父评论ID
        comment.setParentId(commentId);
        postCommentService.addComment(comment);
        return Result.success();
    }
}