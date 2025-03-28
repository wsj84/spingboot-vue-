package com.wang.animeplatform.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageCommentResult {
    private Integer page;
    private Integer pageSize;
}
