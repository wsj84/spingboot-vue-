package com.wang.animeplatform.service;

import com.wang.animeplatform.pojo.Categories;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;

import java.util.List;

public interface CategoriesService {
    List<Categories> findAll();

    PageResult queryCategories(PageAll requestCategoriesPage);

    void addCategories(Categories categories);

    void deleteCategories(Integer id);

    void updateCategories(Categories categories);

    Categories FindbyId(Integer id);
}
