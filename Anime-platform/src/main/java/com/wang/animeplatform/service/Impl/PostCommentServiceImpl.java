package com.wang.animeplatform.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.PostCommentMapper;
import com.wang.animeplatform.mapper.PostMapper;
import com.wang.animeplatform.pojo.PostComment;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PostCommentServiceImpl implements PostCommentService {
    @Autowired
    private PostCommentMapper postCommentMapper;
    
    @Autowired
    private PostMapper postMapper;

    @Override
    public PageResult findByPostId(Integer postId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        // 只查询顶级评论（parentId为null的评论）
        List<PostComment> comments = postCommentMapper.findByPostIdAndParentId(postId, null);

        if (comments != null) {
            Page<PostComment> p = (Page<PostComment>) comments;

            // 查询每个评论的回复
            for (PostComment comment : p.getResult()) {
                List<PostComment> replies = postCommentMapper.findByParentId(comment.getId());
                comment.setReplies(replies);
            }

            return new PageResult(p.getTotal(), p.getResult());
        }
        return null;
    }

    @Override
    @Transactional
    public void addComment(PostComment comment) {
        comment.setCreatedAt(new Date());
        comment.setIsDeleted(0);
        postCommentMapper.addComment(comment);
        
        // 更新帖子评论计数
        postMapper.incrementCommentCount(comment.getPostId());
    }

    @Override
    @Transactional
    public void deleteComment(Integer id) {
        // 获取评论信息，用于更新帖子评论计数
        PostComment comment = postCommentMapper.findById(id);
        if (comment != null) {
            postCommentMapper.deleteComment(id);
            
            // 更新帖子评论计数
            postMapper.decrementCommentCount(comment.getPostId());
        }
    }

    @Override
    public List<PostComment> findByUserId(Integer userId) {
        return postCommentMapper.findByUserId(userId);
    }
}