package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.CommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommentLikeMapper {
    CommentLike findByCommentIdAndUserId(@Param("commentId") Integer commentId, @Param("userId") Integer userId);
    void addLike(CommentLike like);
    void deleteLike(@Param("commentId") Integer commentId, @Param("userId") Integer userId);
    List<Integer> findLikedCommentsByUserId(Integer userId);
}