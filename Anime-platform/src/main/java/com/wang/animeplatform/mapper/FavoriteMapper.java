package com.wang.animeplatform.mapper;


import com.wang.animeplatform.pojo.Favorite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    List<Favorite> findAll();

    Favorite findById(Integer id);

    void addFavorite(Favorite favorite);

    void deleteFavorite(Integer id);

    List<Favorite> findByUserId(Integer userId);

    Favorite checkFavorite(Integer userId, Integer animeId);
}