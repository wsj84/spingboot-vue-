package com.wang.animeplatform.service.Impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.DanmakuMapper;
import com.wang.animeplatform.pojo.Danmaku;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanmakuServiceImpl implements DanmakuService {

    @Autowired
    private DanmakuMapper danmakuMapper;

    @Override
    public List<Danmaku> findAll() {
        return danmakuMapper.findAll();
    }

    @Override
    public PageResult findByqury(PageAll pageAll) {
        PageHelper.startPage(pageAll.getPage(), pageAll.getPageSize());
        List<Danmaku> danmaku = danmakuMapper.findByqury(pageAll);
        Page<Danmaku> p = (Page<Danmaku>) danmaku;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public void addDanmaku(Danmaku danmaku) {
        danmakuMapper.addDanmaku(danmaku);
    }

    @Override
    public void updateDanmaku(Danmaku danmaku) {
        danmakuMapper.updateDanmaku(danmaku);
    }

    @Override
    public void deleteDanmaku(Integer id) {
        danmakuMapper.deleteDanmaku(id);
    }

    @Override
    public List<Danmaku> findByAnimeId(Integer animeId) {
        if (animeId != null) {
            return danmakuMapper.findByAnimeId(animeId);
        }
        return List.of();
    }
}