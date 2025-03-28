package com.wang.animeplatform.service;

import com.wang.animeplatform.pojo.PostComment;
import com.wang.animeplatform.pojo.PageResult;

import java.util.List;

public interface PostCommentService {
    PageResult findByPostId(Integer postId, Integer page, Integer pageSize);
    void addComment(PostComment comment);
    void deleteComment(Integer id);
    List<PostComment> findByUserId(Integer userId);
}