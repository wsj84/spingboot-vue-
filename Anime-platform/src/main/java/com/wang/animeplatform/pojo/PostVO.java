package com.wang.animeplatform.pojo;

import com.wang.animeplatform.pojo.Post;
import com.wang.animeplatform.pojo.User;
import lombok.Data;

@Data
public class PostVO extends Post {
    private User user;
    private Boolean isLiked;
}