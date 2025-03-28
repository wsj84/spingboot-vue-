package com.wang.animeplatform.service;

import com.wang.animeplatform.pojo.Post;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.PostVO;


import java.util.List;

public interface PostService {
    PageResult findPosts(Integer page, Integer pageSize, String category, String keyword);
    PostVO findById(Integer id, Integer userId);
    void addPost(Post post);
    void updatePost(Post post);
    void deletePost(Integer id);
    void incrementViewCount(Integer id);
    void likePost(Integer postId, Integer userId);
    void unlikePost(Integer postId, Integer userId);
    List<Post> findByUserId(Integer userId);
}