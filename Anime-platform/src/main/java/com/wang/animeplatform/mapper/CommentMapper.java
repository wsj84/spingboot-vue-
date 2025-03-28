package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.Comment;
import com.wang.animeplatform.pojo.PageAll;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> findAll();

    Comment findById(Integer id);

    void addComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(Integer id);

    List<Comment> queryComment(PageAll requestPage);
}
