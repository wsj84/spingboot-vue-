package com.wang.animeplatform.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.CommentMapper;
import com.wang.animeplatform.pojo.Comment;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageCommentResult;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findAll() {
        return commentMapper.findAll();
    }

    @Override
    public PageResult queryComment(PageAll requestPage) {
        PageHelper.startPage(requestPage.getPage(), requestPage.getPageSize());
        List<Comment> comment = commentMapper.queryComment(requestPage);
        Page<Comment> p = (Page<Comment>) comment;
        return new PageResult(p.getTotal(), p.getResult());
    }


    @Override
    public Comment findById(Integer id) {
        return commentMapper.findById(id);
    }

    @Override
    public void addComment(Comment comment) {
        commentMapper.addComment(comment);
    }

    @Override
    public void updateComment(Comment comment) {
        commentMapper.updateComment(comment);
    }

    @Override
    public void deleteComment(Integer id) {
        commentMapper.deleteComment(id);
    }
}

