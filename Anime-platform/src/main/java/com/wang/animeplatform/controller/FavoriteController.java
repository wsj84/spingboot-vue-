package com.wang.animeplatform.controller;


import com.wang.animeplatform.pojo.Favorite;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    /* 查询所有收藏 */
    @GetMapping
    public Result findAll() {
        List<Favorite> favorites = favoriteService.findAll();
        return Result.success(favorites);
    }

    /* 根据ID查询收藏 */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        Favorite favorite = favoriteService.findById(id);
        return Result.success(favorite);
    }

    /* 根据用户ID查询收藏 */
    @GetMapping("/user")
    public Result findByUserId(@ModelAttribute PageAll pageAll) {
        PageResult pageResult = favoriteService.findByUserId(pageAll);
        return Result.success(pageResult);
    }

    @GetMapping("/check")
    public Result checkFavorite(@RequestParam Integer userId, @RequestParam Integer animeId) {
        Favorite favorite = favoriteService.checkFavorite(userId, animeId);
        return Result.success(favorite);
    }
    /* 添加收藏 */
    @PostMapping
    public Result addFavorite(@RequestBody Favorite favorite) {
        favoriteService.addFavorite(favorite);
        return Result.success();
    }

    /* 删除收藏 */
    @DeleteMapping("/{id}")
    public Result deleteFavorite(@PathVariable Integer id) {
        favoriteService.deleteFavorite(id);
        return Result.success();
    }
}