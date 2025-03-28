package com.wang.animeplatform.service;

import com.wang.animeplatform.pojo.Comment;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageCommentResult;
import com.wang.animeplatform.pojo.PageResult;

import java.util.List;

public interface CommentService {
    List<Comment> findAll();

    PageResult queryComment(PageAll requestPage);

    Comment findById(Integer id);

    void addComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(Integer id);
}
