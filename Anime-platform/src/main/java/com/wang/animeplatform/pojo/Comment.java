package com.wang.animeplatform.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Integer id;
    private String content;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    @JsonProperty("animeId")
    private Integer animeId;
    @JsonProperty("userId")
    private Integer userId;
}
