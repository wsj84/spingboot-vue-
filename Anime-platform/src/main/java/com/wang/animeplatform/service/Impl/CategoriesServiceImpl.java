package com.wang.animeplatform.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.CategoriesMapper;
import com.wang.animeplatform.pojo.Categories;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    @Autowired
    private CategoriesMapper categoriesMapper;
    @Override
    public List<Categories> findAll() {
        return categoriesMapper.findAll();
    }
    @Override
    public PageResult queryCategories(PageAll requestCategoriesPage) {
        PageHelper.startPage(requestCategoriesPage.getPage(), requestCategoriesPage.getPageSize());
        List<Categories> categories = categoriesMapper.queryCategories();
        Page<Categories> p = (Page<Categories>) categories;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public void addCategories(Categories categories) {
        categoriesMapper.addCategories(categories);
    }

    @Override
    public void deleteCategories(Integer id) {
        categoriesMapper.deleteCategories(id);
    }

    @Override
    public void updateCategories(Categories categories) {
        categoriesMapper.updateCategories(categories);
    }

    @Override
    public Categories FindbyId(Integer id) {
        Categories categories = categoriesMapper.FindbyId(id);
        return categories;
    }
}
