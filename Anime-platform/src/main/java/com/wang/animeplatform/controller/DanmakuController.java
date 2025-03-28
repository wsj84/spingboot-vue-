package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.Danmaku;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/danmaku")
public class DanmakuController {

    @Autowired
    private DanmakuService danmakuService;

    /* 查询所有弹幕 */
    @GetMapping
    public Result findAll() {
        List<Danmaku> danmakus = danmakuService.findAll();
        return Result.success(danmakus);
    }

    /* 根据需求查询弹幕 */
    @GetMapping("/find")
    public Result findByqury(@ModelAttribute PageAll pageAll) {
        PageResult pageResult = danmakuService.findByqury(pageAll);
        return Result.success(pageResult);
    }
    /*根据动漫ID查询弹幕*/
    @GetMapping("/query/{animeId}")
    public Result findByAnimeId(@PathVariable Integer animeId) {
        List<Danmaku> danmakus = danmakuService.findByAnimeId(animeId);
        return Result.success(danmakus);
    }

    /* 添加弹幕 */
    @PostMapping("/send")
    public Result addDanmaku(@RequestBody Danmaku danmaku) {
        danmakuService.addDanmaku(danmaku);
        return Result.success();
    }

    /* 更新弹幕 */
    @PutMapping
    public Result updateDanmaku(@RequestBody Danmaku danmaku) {
        danmakuService.updateDanmaku(danmaku);
        return Result.success();
    }

    /* 删除弹幕 */
    @DeleteMapping("/{id}")
    public Result deleteDanmaku(@PathVariable Integer id) {
        danmakuService.deleteDanmaku(id);
        return Result.success();
    }
}