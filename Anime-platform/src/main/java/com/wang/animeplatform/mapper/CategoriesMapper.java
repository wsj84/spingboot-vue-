package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.Categories;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoriesMapper {

    List<Categories> findAll();

    void addCategories(Categories categories);

    void deleteCategories(Integer id);

    void updateCategories(Categories categories);

    Categories FindbyId(Integer id);

    List<Categories> queryCategories();
}
