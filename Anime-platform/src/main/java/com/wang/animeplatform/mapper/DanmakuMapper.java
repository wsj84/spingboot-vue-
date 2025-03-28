package com.wang.animeplatform.mapper;


import com.wang.animeplatform.pojo.Danmaku;
import com.wang.animeplatform.pojo.PageAll;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DanmakuMapper {

    List<Danmaku> findAll();

    List<Danmaku> findByqury(PageAll pageAll);

    void addDanmaku(Danmaku danmaku);

    void updateDanmaku(Danmaku danmaku);

    void deleteDanmaku(Integer id);

    List<Danmaku> findByAnimeId(Integer animeId);
}