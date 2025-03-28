package com.wang.animeplatform.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchHistory {
    private Integer id;
    @JsonProperty("watchedAt")
    private LocalDateTime watchedAt;
    @JsonProperty("animeId")
    private Integer animeId;
    @JsonProperty("userId")
    private Integer userId;

    /*动漫实体*/
    private String title;
    @JsonProperty("isVipOnly")
    private Boolean isVipOnly;
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
}
