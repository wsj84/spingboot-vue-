package com.wang.animeplatform.service;



import com.github.pagehelper.Page;
import com.wang.animeplatform.pojo.Danmaku;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;

import java.util.List;

public interface DanmakuService {

    List<Danmaku> findAll();

    PageResult findByqury(PageAll pageAll);

    void addDanmaku(Danmaku danmaku);

    void updateDanmaku(Danmaku danmaku);

    void deleteDanmaku(Integer id);

    List<Danmaku> findByAnimeId(Integer animeId);
}