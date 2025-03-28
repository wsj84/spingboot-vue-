package com.wang.animeplatform.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.controller.UserController;
import com.wang.animeplatform.mapper.ReportMapper;
import com.wang.animeplatform.pojo.*;
import com.wang.animeplatform.service.ReportService;
import com.wang.animeplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<Report> findAll() {
        return reportMapper.findAll();
    }

    @Override
    public Report findById(Integer id) {
        return reportMapper.findById(id);
    }

    @Override
    public void addReport(Report report) {
        reportMapper.addReport(report);
    }

    @Override
    public void deleteReport(Integer id) {
        reportMapper.deleteReport(id);
    }

    @Override
    public PageResult findByCondition(PageAll pageAnimeResult) {
        PageHelper.startPage(pageAnimeResult.getPage(), pageAnimeResult.getPageSize());
        List<Report> reportResults = reportMapper.findByCondition(pageAnimeResult);
        if (reportResults != null){
            Page<Report> p = (Page<Report>) reportResults;
            return new PageResult(p.getTotal(), p.getResult());
            }
            return null;
        }


}