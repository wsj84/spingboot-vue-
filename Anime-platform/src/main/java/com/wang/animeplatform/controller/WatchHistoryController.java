package com.wang.animeplatform.controller;


import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.pojo.WatchHistory;
import com.wang.animeplatform.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryService watchHistoryService;

    /* 查询所有观看历史 */
    @GetMapping
    public Result findAll() {
        List<WatchHistory> watchHistories = watchHistoryService.findAll();
        return Result.success(watchHistories);
    }

    /* 根据ID查询观看历史 */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        WatchHistory watchHistory = watchHistoryService.findById(id);
        return Result.success(watchHistory);
    }
    /* 根据用户ID查询观看历史 */
    @GetMapping("/user")
    public Result findByUserId(@ModelAttribute PageAll pageAll) {
        PageResult watchHistories = watchHistoryService.findByUserId(pageAll);
        return Result.success(watchHistories);
    }

    /* 添加观看历史 */
    @PostMapping
    public Result addWatchHistory(@RequestBody WatchHistory watchHistory) {
        watchHistoryService.addWatchHistory(watchHistory);
        return Result.success();
    }

    /* 删除观看历史 */
    @DeleteMapping("/{id}")
    public Result deleteWatchHistory(@PathVariable Integer id) {
        watchHistoryService.deleteWatchHistory(id);
        return Result.success();
    }
}