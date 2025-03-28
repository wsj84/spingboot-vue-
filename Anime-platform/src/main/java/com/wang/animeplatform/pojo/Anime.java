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
public class Anime {
    private Integer id;
    private String title;
    private String description;
    @JsonProperty("videoUrl")
    private String videoUrl;
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("releaseDate")
    private LocalDate releaseDate;
    @JsonProperty("viewCount")
    private Integer viewCount;
    @JsonProperty("isVipOnly")
    private boolean isVipOnly;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("categoryId")
    private Integer categoryId;
}
