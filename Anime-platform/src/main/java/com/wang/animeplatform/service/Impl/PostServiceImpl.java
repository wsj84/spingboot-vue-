package com.wang.animeplatform.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.PostLikeMapper;
import com.wang.animeplatform.mapper.PostMapper;
import com.wang.animeplatform.pojo.Post;
import com.wang.animeplatform.pojo.PostLike;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.PostVO;
import com.wang.animeplatform.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private PostLikeMapper postLikeMapper;

    @Override
    public PageResult findPosts(Integer page, Integer pageSize, String category, String keyword) {
        try {
            PageHelper.startPage(page, pageSize);
            List<PostVO> posts = postMapper.findPosts(category, keyword);
            if (posts != null) {
                Page<PostVO> p = (Page<PostVO>) posts;
                return new PageResult(p.getTotal(), p.getResult());
            }
            return new PageResult(0, Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(0, Collections.emptyList());
        }
    }

    @Override
    public PostVO findById(Integer id, Integer userId) {
        // 获取帖子信息
        Post post = postMapper.findById(id);
        if (post == null) {
            return null;
        }
        
        // 转换为VO
        PostVO postVO = new PostVO();
        // 复制属性
        copyProperties(post, postVO);
        
        // 如果有用户ID，检查是否点赞
        if (userId != null) {
            PostLike like = postLikeMapper.findByPostIdAndUserId(id, userId);
            postVO.setIsLiked(like != null);
        } else {
            postVO.setIsLiked(false);
        }
        
        return postVO;
    }

    @Override
    @Transactional
    public void addPost(Post post) {
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCreatedAt(new Date());
        post.setIsDeleted(0);
        postMapper.addPost(post);
    }

    @Override
    public void updatePost(Post post) {
        postMapper.updatePost(post);
    }

    @Override
    public void deletePost(Integer id) {
        postMapper.deletePost(id);
    }

    @Override
    public void incrementViewCount(Integer id) {
        postMapper.incrementViewCount(id);
    }

    @Override
    @Transactional
    public void likePost(Integer postId, Integer userId) {
        // 检查是否已点赞
        PostLike existingLike = postLikeMapper.findByPostIdAndUserId(postId, userId);
        if (existingLike == null) {
            // 添加点赞记录
            PostLike like = new PostLike();
            like.setPostId(postId);
            like.setUserId(userId);
            like.setCreatedAt(new Date());
            postLikeMapper.addLike(like);
            
            // 更新帖子点赞计数
            postMapper.incrementLikeCount(postId);
        }
    }

    @Override
    @Transactional
    public void unlikePost(Integer postId, Integer userId) {
        // 检查是否已点赞
        PostLike existingLike = postLikeMapper.findByPostIdAndUserId(postId, userId);
        if (existingLike != null) {
            // 删除点赞记录
            postLikeMapper.deleteLike(postId, userId);
            
            // 更新帖子点赞计数
            postMapper.decrementLikeCount(postId);
        }
    }

    @Override
    public List<Post> findByUserId(Integer userId) {
        return postMapper.findByUserId(userId);
    }
    
    private void copyProperties(Post source, PostVO target) {
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setContent(source.getContent());
        target.setCategory(source.getCategory());
        target.setUserId(source.getUserId());
        target.setViewCount(source.getViewCount());
        target.setLikeCount(source.getLikeCount());
        target.setCommentCount(source.getCommentCount());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setIsDeleted(source.getIsDeleted());
    }
}