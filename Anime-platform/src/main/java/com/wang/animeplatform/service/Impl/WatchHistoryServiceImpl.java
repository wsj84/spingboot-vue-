package com.wang.animeplatform.service.Impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.WatchHistoryMapper;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.WatchHistory;
import com.wang.animeplatform.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WatchHistoryServiceImpl implements WatchHistoryService {

    @Autowired
    private WatchHistoryMapper watchHistoryMapper;

    @Override
    public List<WatchHistory> findAll() {
        return watchHistoryMapper.findAll();
    }

    @Override
    public WatchHistory findById(Integer id) {
        return watchHistoryMapper.findById(id);
    }

    @Transactional
    @Override
    public void addWatchHistory(WatchHistory watchHistory) {
        // 检查是否已经存在相同动漫的观看记录
        List<WatchHistory> existingRecords = watchHistoryMapper.findByAnimeIdAndUserId(watchHistory.getAnimeId(), watchHistory.getUserId());
        System.out.println("existingRecord:" + existingRecords);
        if (existingRecords != null && !existingRecords.isEmpty()) {
            // 遍历并删除所有旧记录
            for (WatchHistory existingRecord : existingRecords) {
                int i = watchHistoryMapper.deleteWatchHistory(existingRecord.getId());
                if (i == 0) {
                    // 如果删除失败，抛出异常回滚事务
                    throw new RuntimeException("Failed to delete existing watch history record.");
                }
            }
        }
        watchHistoryMapper.addWatchHistory(watchHistory);
    }
    @Override
    public void deleteWatchHistory(Integer id) {
        watchHistoryMapper.deleteWatchHistory(id);
    }

    @Override
    public void dieletebyIdAndUserId(Integer id, Integer userId) {
        watchHistoryMapper.dieletebyIdAndUserId(id, userId);
    }


    @Override
    public PageResult findByUserId(PageAll pageAll) {
        PageHelper.startPage(pageAll.getPage(), pageAll.getPageSize());
        List<WatchHistory> watchHistory = watchHistoryMapper.findByUserId(pageAll);
        if (watchHistory != null) {
            Page<WatchHistory> p = (Page<WatchHistory>) watchHistory;
            return new PageResult(p.getTotal(), p.getResult());
        }
        return null;
    }
}