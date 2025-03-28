package com.wang.animeplatform.mapper;


import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.Report;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {

    List<Report> findAll();

    Report findById(Integer id);

    void addReport(Report report);

    void deleteReport(Integer id);

    List<Report> findByCondition(PageAll pageAnimeResult);
}