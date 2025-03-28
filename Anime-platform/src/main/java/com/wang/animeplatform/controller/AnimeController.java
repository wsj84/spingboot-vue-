package com.wang.animeplatform.controller;

import com.github.pagehelper.Page;
import com.wang.animeplatform.pojo.*;
import com.wang.animeplatform.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animes")
public class AnimeController {
    @Autowired
    private AnimeService animeService;

    /* 查询所有动漫 */
    @GetMapping("/all")
    public Result findAll() {
        List<Anime> animes = animeService.findAll();
        return Result.success(animes);
    }
    /*根据需要查询动漫*/
    @GetMapping("/query")
    public Result findByCondition(PageAll pageAnimeResult){
        PageResult pageResult = animeService.queryAnime(pageAnimeResult);
        return Result.success(pageResult);
    }
    /*根据观看次数查询弹幕*/
    @GetMapping("/ranking")
    public Result findByViewCount(@RequestParam Integer page, @RequestParam Integer pageSize) {
        PageResult animes = animeService.findByViewCount(page, pageSize);
        return Result.success(animes);
    }
     /*根据ID查询动漫 */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        Anime anime = animeService.findById(id);
        return Result.success(anime);
    }
    /*根据动漫类别查询动漫*/
    @GetMapping("/random")
    public Result findByType(@RequestParam Integer categoryId, @RequestParam Integer limit ,@RequestParam Integer excludeId) {
        List<Anime> animes = animeService.findByType(categoryId, limit, excludeId);
        return Result.success(animes);
    }

     /*添加动漫 */
    @PostMapping
    public Result addAnime(@RequestBody Anime anime) {
        animeService.addAnime(anime);
        return Result.success();
    }

     /*更新动漫 */
    @PutMapping
    public Result updateAnime(@RequestBody Anime anime) {
        animeService.updateAnime(anime);
        return Result.success();
   }
   /*更行动漫观看次数*/
    @PutMapping("/incrementView/{animeId}")
    public Result uapdateViewCount(@PathVariable Integer animeId){
        animeService.uapdateViewCount(animeId);
        return Result.success();
    }

     /*删除动漫 */
    @DeleteMapping("/{id}")
    public Result deleteAnime(@PathVariable Integer id) {
        animeService.deleteAnime(id);
        return Result.success();
    }
}
