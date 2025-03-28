package com.wang.animeplatform.pojo;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PostComment {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String content;
    private Date createdAt;
    private Integer isDeleted;
    private Integer parentId; // 父评论ID
    // 非数据库字段，用于存储回复列表
    private transient List<PostComment> replies;
    private transient User user;  // 评论用户信息
    private Integer likeCount;  // 添加点赞计数字段
}