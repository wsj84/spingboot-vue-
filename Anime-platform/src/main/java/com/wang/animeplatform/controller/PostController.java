package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.Post;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.PostVO;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    /**
     * 获取帖子列表
     */
    @GetMapping
    public Result getPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {

        PageResult pageResult = postService.findPosts(page, pageSize, category, keyword);
        return Result.success(pageResult);
    }

    /**
     * 获取帖子详情
     */
    @GetMapping("/{id}")
    public Result getPostDetail(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer userId) {
        PostVO post = postService.findById(id, userId);
        if (post != null) {
            // 增加浏览量
            postService.incrementViewCount(id);
            return Result.success(post);
        }
        return Result.error("帖子不存在");
    }

    /**
     * 创建帖子
     */
    @PostMapping
    public Result createPost(@RequestBody Post post) {
        postService.addPost(post);
        return Result.success(post);
    }

    /**
     * 更新帖子
     */
    @PutMapping
    public Result updatePost(@RequestBody Post post) {
        postService.updatePost(post);
        return Result.success();
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return Result.success();
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/{id}/like")
    public Result likePost(@PathVariable Integer id, @RequestParam Integer userId) {
        postService.likePost(id, userId);
        return Result.success();
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/{id}/like")
    public Result unlikePost(@PathVariable Integer id, @RequestParam Integer userId) {
        postService.unlikePost(id, userId);
        return Result.success();
    }

    /**
     * 获取用户的帖子
     */
    @GetMapping("/user/{userId}")
    public Result getUserPosts(@PathVariable Integer userId) {
        List<Post> posts = postService.findByUserId(userId);
        return Result.success(posts);
    }
}