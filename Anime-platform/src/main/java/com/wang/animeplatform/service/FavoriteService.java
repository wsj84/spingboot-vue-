package com.wang.animeplatform.service;


import com.wang.animeplatform.pojo.Favorite;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;

import java.util.List;

public interface FavoriteService {

    List<Favorite> findAll();

    Favorite findById(Integer id);

    void addFavorite(Favorite favorite);

    void deleteFavorite(Integer id);

    PageResult findByUserId(PageAll pageAll);

    Favorite checkFavorite(Integer userId, Integer animeId);
}