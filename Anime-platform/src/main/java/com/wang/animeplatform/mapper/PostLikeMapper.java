package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostLikeMapper {
    PostLike findByPostIdAndUserId(@Param("postId") Integer postId, @Param("userId") Integer userId);
    void addLike(PostLike like);
    void deleteLike(@Param("postId") Integer postId, @Param("userId") Integer userId);
    List<Integer> findLikedPostsByUserId(Integer userId);
}