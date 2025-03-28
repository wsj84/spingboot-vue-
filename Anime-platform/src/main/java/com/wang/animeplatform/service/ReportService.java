package com.wang.animeplatform.service;


import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Report;

import java.util.List;

public interface ReportService {

    List<Report> findAll();

    Report findById(Integer id);

    void addReport(Report report);

    void deleteReport(Integer id);

    PageResult findByCondition(PageAll pageAnimeResult);
}