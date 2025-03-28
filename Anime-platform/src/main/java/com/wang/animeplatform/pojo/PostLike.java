package com.wang.animeplatform.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class PostLike {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private Date createdAt;
}