package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.Anime;
import com.wang.animeplatform.pojo.PageAll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnimeMapper {
    List<Anime> findAll();

    Anime findById(Integer id);

    void addAnime(Anime anime);

    void updateAnime(Anime anime);

    void deleteAnime(Integer id);

    List<Anime> queryAnime(PageAll pageAnimeResult);

    List<Anime> findByType(Integer categoryId, Integer limit, Integer excludeId);

    void uapdateViewCount(Integer animeId);

    List<Anime> findByViewCount();
}
