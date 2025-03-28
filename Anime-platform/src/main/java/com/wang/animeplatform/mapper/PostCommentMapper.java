package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.PostComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostCommentMapper {
    List<PostComment> findByPostId(Integer postId);
    PostComment findById(Integer id);
    void addComment(PostComment comment);
    void deleteComment(Integer id);
    List<PostComment> findByUserId(Integer userId);
    void incrementLikeCount(Integer id);
    void decrementLikeCount(Integer id);
    // 添加新方法用于支持回复功能
    List<PostComment> findByPostIdAndParentId(@Param("postId") Integer postId,
                                              @Param("parentId") Integer parentId);
    List<PostComment> findByParentId(Integer parentId);
}