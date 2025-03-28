package com.wang.animeplatform.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Danmaku {
    private Integer id;
    @JsonProperty("animeId")
    private Integer animeId;
    @JsonProperty("userId")
    private Integer userId;
    private String content;
    private Float time;
    private String color;
    @JsonProperty("fontSize")
    private Integer fontSize;
    @JsonProperty("isMe")
    private Boolean isMe;

    private String type;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}
