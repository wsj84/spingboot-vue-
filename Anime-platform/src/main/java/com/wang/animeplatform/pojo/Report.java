package com.wang.animeplatform.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private Integer id;
    private String reason;
    @JsonProperty("reportedAt")
    private LocalDate reportedAt;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("reportedUserId")
    private Integer reportedUserId;
    @JsonProperty("commentId")
    private Integer commentId;
}
