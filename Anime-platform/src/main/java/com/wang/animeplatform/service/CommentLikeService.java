package com.wang.animeplatform.service;

import java.util.List;

public interface CommentLikeService {
    void likeComment(Integer commentId, Integer userId);
    void unlikeComment(Integer commentId, Integer userId);
    boolean isCommentLiked(Integer commentId, Integer userId);

    List<Integer> findLikedCommentsByUserId(Integer userId);
}