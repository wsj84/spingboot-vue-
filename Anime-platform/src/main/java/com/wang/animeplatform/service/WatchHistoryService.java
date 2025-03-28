package com.wang.animeplatform.service;


import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.WatchHistory;

import java.util.List;

public interface WatchHistoryService {

    List<WatchHistory> findAll();

    WatchHistory findById(Integer id);

    void addWatchHistory(WatchHistory watchHistory);

    void deleteWatchHistory(Integer id);

    void dieletebyIdAndUserId(Integer id,Integer userId);

    PageResult findByUserId(PageAll pageAll);
}