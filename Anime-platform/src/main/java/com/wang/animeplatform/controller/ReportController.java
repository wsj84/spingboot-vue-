package com.wang.animeplatform.controller;


import com.wang.animeplatform.pojo.*;
import com.wang.animeplatform.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /* 查询所有举报 */
    @GetMapping
    public Result findAll() {
        List<Report> reports = reportService.findAll();
        return Result.success(reports);
    }

    /* 根据ID查询举报 */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        Report report = reportService.findById(id);
        return Result.success(report);
    }
/*    根据需求查询代码*/
    @GetMapping("/query")
    public Result findByCondition(PageAll pageAnimeResult){
        PageResult reports = reportService.findByCondition(pageAnimeResult);
        return Result.success(reports);
    }

    /* 添加举报 */
    @PostMapping
    public Result addReport(@RequestBody Report report) {
        reportService.addReport(report);
        return Result.success();
    }

    /* 删除举报 */
    @DeleteMapping("/{id}")
    public Result deleteReport(@PathVariable Integer id) {
        reportService.deleteReport(id);
        return Result.success();
    }
}