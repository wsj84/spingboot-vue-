package com.wang.animeplatform.service;

import com.wang.animeplatform.pojo.Anime;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;

import java.util.List;

public interface AnimeService {
    List<Anime> findAll();

    PageResult queryAnime(PageAll pageAnimeResult);

    Anime findById(Integer id);

    void addAnime(Anime anime);

    void updateAnime(Anime anime);

    void deleteAnime(Integer id);

    List<Anime> findByType(Integer categoryId, Integer limit, Integer excludeId);

    void uapdateViewCount(Integer animeId);

    PageResult findByViewCount(Integer page, Integer pageSize);
}
