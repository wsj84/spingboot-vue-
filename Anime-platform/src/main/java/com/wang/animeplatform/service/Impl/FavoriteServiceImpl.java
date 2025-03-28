package com.wang.animeplatform.service.Impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.FavoriteMapper;
import com.wang.animeplatform.pojo.Favorite;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public List<Favorite> findAll() {
        return favoriteMapper.findAll();
    }

    @Override
    public Favorite findById(Integer id) {
        return favoriteMapper.findById(id);
    }

    @Override
    public void addFavorite(Favorite favorite) {
        favoriteMapper.addFavorite(favorite);
    }

    @Override
    public void deleteFavorite(Integer id) {
        favoriteMapper.deleteFavorite(id);
    }

    @Override
    public PageResult findByUserId(PageAll pageAll) {
        PageHelper.startPage(pageAll.getPage(), pageAll.getPageSize());
        List<Favorite> favorites = favoriteMapper.findByUserId(pageAll.getUserId());
        Page<Favorite> p = (Page<Favorite>) favorites;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public Favorite checkFavorite(Integer userId, Integer animeId) {
        Favorite favorite = favoriteMapper.checkFavorite(userId,animeId);
        if (favorite != null) {
            favorite.setIsFavorite(true);
            return favorite;
        }else {
            favorite = new Favorite();
            favorite.setIsFavorite(false);
            return favorite;
        }
    }
}