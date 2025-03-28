package com.wang.animeplatform.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    private Integer id;
    @JsonProperty("animeId")
    private Integer animeId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("favoritedAt")
    private LocalDateTime favoritedAt;

    /*动漫实体*/
    private String title;
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("isVipOnly")
    private Boolean isVipOnly;

    /*仅在浏览量查询是否收藏时使用，数据库不存在次字段*/
    private Boolean isFavorite = false;
}
