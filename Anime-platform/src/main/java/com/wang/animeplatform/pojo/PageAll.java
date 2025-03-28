package com.wang.animeplatform.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageAll {
    private Integer page;
    private Integer pageSize;

    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("reportedUserId")
    private Integer reportedUserId;
    @JsonProperty("animeId")
    private Integer animeId;
    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("commentId")
    private Integer commentId;
    @JsonProperty("keyword")
    private String keyword;
}
