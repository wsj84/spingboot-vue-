package com.wang.animeplatform.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class Post {
    private Integer id;
    private String title;
    private String content;
    private String category;
    private Integer userId;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Date createdAt;
    private Date updatedAt;
    private Integer isDeleted;
}