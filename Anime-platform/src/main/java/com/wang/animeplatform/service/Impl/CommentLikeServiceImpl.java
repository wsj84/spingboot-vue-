package com.wang.animeplatform.service.Impl;

import com.wang.animeplatform.mapper.CommentLikeMapper;
import com.wang.animeplatform.mapper.PostCommentMapper;
import com.wang.animeplatform.pojo.CommentLike;
import com.wang.animeplatform.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {
    @Autowired
    private CommentLikeMapper commentLikeMapper;
    
    @Autowired
    private PostCommentMapper postCommentMapper;

    @Override
    @Transactional
    public void likeComment(Integer commentId, Integer userId) {
        // 检查是否已点赞
        CommentLike existingLike = commentLikeMapper.findByCommentIdAndUserId(commentId, userId);
        if (existingLike == null) {
            // 添加点赞记录
            CommentLike like = new CommentLike();
            like.setCommentId(commentId);
            like.setUserId(userId);
            like.setCreatedAt(new Date());
            commentLikeMapper.addLike(like);
            
            // 更新评论点赞计数（需要在PostCommentMapper中添加相应方法）
            postCommentMapper.incrementLikeCount(commentId);
        }
    }

    @Override
    @Transactional
    public void unlikeComment(Integer commentId, Integer userId) {
        // 检查是否已点赞
        CommentLike existingLike = commentLikeMapper.findByCommentIdAndUserId(commentId, userId);
        if (existingLike != null) {
            // 删除点赞记录
            commentLikeMapper.deleteLike(commentId, userId);
            
            // 更新评论点赞计数（需要在PostCommentMapper中添加相应方法）
            postCommentMapper.decrementLikeCount(commentId);
        }
    }

    @Override
    public boolean isCommentLiked(Integer commentId, Integer userId) {
        return commentLikeMapper.findByCommentIdAndUserId(commentId, userId) != null;
    }

    @Override
    public List<Integer> findLikedCommentsByUserId(Integer userId) {
        return commentLikeMapper.findLikedCommentsByUserId(userId);
    }
}