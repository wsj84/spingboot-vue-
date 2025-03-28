package com.wang.animeplatform.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class CommentLike {
    private Integer id;
    private Integer commentId;
    private Integer userId;
    private Date createdAt;
}