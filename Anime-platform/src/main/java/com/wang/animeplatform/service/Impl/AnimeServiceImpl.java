package com.wang.animeplatform.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.AnimeMapper;
import com.wang.animeplatform.pojo.Anime;
import com.wang.animeplatform.pojo.PageAll;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
@Slf4j
@Service
public class AnimeServiceImpl implements AnimeService {
    @Autowired
    private AnimeMapper animeMapper;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Override
    public List<Anime> findAll() {
        return animeMapper.findAll();
    }

    @Override
    public PageResult queryAnime(PageAll pageAnimeResult) {
        PageHelper.startPage(pageAnimeResult.getPage(), pageAnimeResult.getPageSize());
        List<Anime> anime = animeMapper.queryAnime(pageAnimeResult);
        if (anime != null) {
            Page<Anime> p = (Page<Anime>) anime;
            return new PageResult(p.getTotal(), p.getResult());
        }

        return null;
    }

    @Override
    public Anime findById(Integer id) {
        return animeMapper.findById(id);
    }

    @Override
    public void addAnime(Anime anime) {
        animeMapper.addAnime(anime);
    }

    @Override
    public void updateAnime(Anime anime) {
        animeMapper.updateAnime(anime);
    }

    @Override
    public void deleteAnime(Integer id) {
        Anime anime = animeMapper.findById(id);

        animeMapper.deleteAnime(id);
        String thumbnailUrl = anime.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            // 提取文件名
            String fileName = thumbnailUrl.substring(thumbnailUrl.lastIndexOf("/") + 1);
            File file = new File(uploadDir, fileName);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println();
                } else {
                    log.warn("无法删除封面图片: {}", file.getAbsolutePath());
                }
            } else {
                log.warn("封面图片不存在: {}", file.getAbsolutePath());
            }
        }
    }

    @Override
    public List<Anime> findByType(Integer categoryId, Integer limit, Integer excludeId) {
        if (categoryId != null) {
            return animeMapper.findByType(categoryId, limit, excludeId);
        }
        return null;
    }

    @Override
    public void uapdateViewCount(Integer animeId) {
        animeMapper.uapdateViewCount(animeId);
    }

    @Override
    public PageResult findByViewCount(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Anime> anime = animeMapper.findByViewCount();
        if (anime != null) {
            Page<Anime> p = (Page<Anime>) anime;
            return new PageResult(p.getTotal(), p.getResult());
        }
        return null;
    }
}
