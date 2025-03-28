package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.WatchHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WatchHistoryMapper {
    List<WatchHistory> findAll();

    WatchHistory findById(Integer id);

    void addWatchHistory(WatchHistory watchHistory);

    int deleteWatchHistory(Integer id);

    List<WatchHistory> findByUserId(PageAll pageAll);

    List<WatchHistory>  findByAnimeIdAndUserId(Integer animeId, Integer userId);

    int dieletebyIdAndUserId(Integer id,Integer userId);
}
