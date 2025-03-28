package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-comments")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    /**
     * 点赞评论
     */
    @PostMapping("/{commentId}/like")
    public Result likeComment(
            @PathVariable Integer commentId,
            @RequestParam Integer userId) {
        commentLikeService.likeComment(commentId, userId);
        return Result.success();
    }

    /**
     * 取消点赞评论
     */
    @DeleteMapping("/{commentId}/like")
    public Result unlikeComment(
            @PathVariable Integer commentId,
            @RequestParam Integer userId) {
        commentLikeService.unlikeComment(commentId, userId);
        return Result.success();
    }

    @GetMapping("/liked")
    public Result getLikedComments(@RequestParam Integer userId) {
        List<Integer> likedCommentIds = commentLikeService.findLikedCommentsByUserId(userId);
        return Result.success(likedCommentIds);
    }
}