package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.Post;

import com.wang.animeplatform.pojo.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostMapper {
    List<PostVO> findPosts(@Param("category") String category, @Param("keyword") String keyword);
    Post findById(Integer id);
    void addPost(Post post);
    void updatePost(Post post);
    void deletePost(Integer id);
    void incrementViewCount(Integer id);
    void incrementCommentCount(Integer id);
    void decrementCommentCount(Integer id);
    void incrementLikeCount(Integer id);
    void decrementLikeCount(Integer id);
    List<Post> findByUserId(Integer userId);
}