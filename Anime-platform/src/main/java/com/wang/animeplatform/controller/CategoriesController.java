package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.*;
import com.wang.animeplatform.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;
    @GetMapping("/all")
    public Result FindAll(){
        return Result.success(categoriesService.findAll());
    }
    @GetMapping("/{id}")
    public Result FindbyId(@PathVariable Integer id){
        Categories categories = categoriesService.FindbyId(id);
        return Result.success(categories);
    }
    @GetMapping("/query")
    public Result queryCategories(PageAll requestCategoriesPage){
        PageResult pageResult = categoriesService.queryCategories(requestCategoriesPage);
        return Result.success(pageResult);
    }


    @PostMapping
    public Result addCategories(@RequestBody Categories categories){
        categoriesService.addCategories(categories);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteCategories(@PathVariable Integer id){
        categoriesService.deleteCategories(id);
        return Result.success();
    }
    @PutMapping
    public Result updateCategories(@RequestBody Categories categories){
        categoriesService.updateCategories(categories);
        return Result.success();
    }
}
