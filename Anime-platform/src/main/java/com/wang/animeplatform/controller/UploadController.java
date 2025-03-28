package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class UploadController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    // 临时文件存储路径
    private static final String TEMP_DIR = "G:\\Code\\毕设\\Anime-platform\\src\\main\\resources\\static\\temp";

    // 临时文件映射表：sessionId -> List<临时文件信息>
    private static final Map<String, Map<String, TempFileInfo>> tempFilesMap = new ConcurrentHashMap<>();

    // 定时清理未确认的临时文件
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        // 创建临时目录
        new File(TEMP_DIR).mkdirs();

        // 启动定时任务，每小时清理一次超过2小时的临时文件
        scheduler.scheduleAtFixedRate(() -> {
            try {
                cleanupExpiredTempFiles();
            } catch (Exception e) {
                log.error("清理临时文件失败", e);
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    /**
     * 文件上传 - 仅临时存储
     */
    @PostMapping("/upload")
    public Result upload(
            MultipartFile file,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) throws IOException {

        log.info("接收文件: {}, sessionId: {}", file.getOriginalFilename(), sessionId);

        // 如果没有提供会话ID，生成一个新的
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 新的文件名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;

        // 创建临时目录（如果不存在）
        File tempSessionDir = new File(TEMP_DIR, sessionId);
        if (!tempSessionDir.exists()) {
            tempSessionDir.mkdirs();
        }

        // 保存到临时文件
        File tempFile = new File(tempSessionDir, newFileName);
        file.transferTo(tempFile);

        // 确定最终文件类型和存储路径
        String fileType = "image"; // 默认为图片
        String targetDirPath = uploadDir;
        String fileUrlPrefix = baseUrl;

        if (extension.equalsIgnoreCase(".mp4") || extension.equalsIgnoreCase(".avi") || extension.equalsIgnoreCase(".mov")) {
            fileType = "video";
            targetDirPath = "G:\\Code\\毕设\\Anime-platform\\src\\main\\resources\\static\\video";
            fileUrlPrefix = "http://localhost:8080/video";
        }

        // 最终文件URL
        String finalFileUrl = fileUrlPrefix + "/" + newFileName;

        // 保存临时文件信息
        TempFileInfo fileInfo = new TempFileInfo();
        fileInfo.setOriginalFilename(originalFilename);
        fileInfo.setTempFilePath(tempFile.getAbsolutePath());
        fileInfo.setTargetDirPath(targetDirPath);
        fileInfo.setNewFileName(newFileName);
        fileInfo.setFileType(fileType);
        fileInfo.setFileUrl(finalFileUrl);
        fileInfo.setCreateTime(System.currentTimeMillis());

        // 将文件信息存入映射表
        tempFilesMap.computeIfAbsent(sessionId, k -> new HashMap<>())
                .put(newFileName, fileInfo);

        // 返回临时文件信息
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("tempFileId", newFileName);
        data.put("fileUrl", finalFileUrl); // 前端仍然使用相同的URL，但实际上是临时的

        return Result.success(data);
    }

    /**
     * 确认保存上传的文件
     */
    @PostMapping("/confirm-upload")
    public Result confirmUpload(
            @RequestBody Map<String, Object> request,
            @RequestHeader("X-Session-Id") String sessionId) {

        log.info("确认上传文件, sessionId: {}, request: {}", sessionId, request);


        @SuppressWarnings("unchecked")
        Map<String, String> fileIds = (Map<String, String>) request.get("fileIds");

        if (fileIds == null || fileIds.isEmpty()) {
            return Result.success("没有要确认的文件");
        }

        Map<String, TempFileInfo> sessionFiles = tempFilesMap.get(sessionId);
        if (sessionFiles == null || sessionFiles.isEmpty()) {
            return Result.success("未找到临时文件记录");
        }

        Map<String, String> confirmedFiles = new HashMap<>();

        for (Map.Entry<String, String> entry : fileIds.entrySet()) {
            String fieldName = entry.getKey(); // 例如 "thumbnailUrl", "videoUrl"
            String tempFileId = entry.getValue();

            TempFileInfo fileInfo = sessionFiles.get(tempFileId);
            if (fileInfo != null) {
                try {
                    // 创建目标文件夹（如果不存在）
                    File targetDir = new File(fileInfo.getTargetDirPath());
                    if (!targetDir.exists()) {
                        targetDir.mkdirs();
                    }

                    // 将临时文件移动到最终位置
                    File sourceFile = new File(fileInfo.getTempFilePath());
                    File targetFile = new File(targetDir, fileInfo.getNewFileName());

                    Files.move(sourceFile.toPath(), targetFile.toPath());

                    // 记录已确认的文件
                    confirmedFiles.put(fieldName, fileInfo.getFileUrl());

                    // 从临时文件映射表中移除
                    sessionFiles.remove(tempFileId);

                } catch (IOException e) {
                    log.error("移动文件失败: " + fileInfo.getTempFilePath(), e);
                    return Result.error("文件确认失败: " + e.getMessage());
                }
            }
        }

        // 如果会话中的所有文件都已确认，移除会话记录
        if (sessionFiles.isEmpty()) {
            tempFilesMap.remove(sessionId);

            // 删除临时目录
            try {
                Files.deleteIfExists(Paths.get(TEMP_DIR, sessionId));
            } catch (IOException e) {
                log.warn("删除临时目录失败: " + sessionId, e);
            }
        }

        return Result.success(confirmedFiles);
    }

    /**
     * 取消上传（删除临时文件）
     */
    @PostMapping("/cancel-upload")
    public Result cancelUpload(
            @RequestParam(required = false) String tempFileId,
            @RequestHeader("X-Session-Id") String sessionId) {

        Map<String, TempFileInfo> sessionFiles = tempFilesMap.get(sessionId);
        if (sessionFiles == null || sessionFiles.isEmpty()) {
            return Result.success("没有临时文件需要取消");
        }

        // 如果指定了特定文件ID，只删除该文件
        if (tempFileId != null && !tempFileId.isEmpty()) {
            TempFileInfo fileInfo = sessionFiles.remove(tempFileId);
            if (fileInfo != null) {
                try {
                    Files.deleteIfExists(Paths.get(fileInfo.getTempFilePath()));
                } catch (IOException e) {
                    log.warn("删除临时文件失败: " + fileInfo.getTempFilePath(), e);
                }
            }
        } else {
            // 否则删除该会话的所有临时文件
            for (TempFileInfo fileInfo : sessionFiles.values()) {
                try {
                    Files.deleteIfExists(Paths.get(fileInfo.getTempFilePath()));
                } catch (IOException e) {
                    log.warn("删除临时文件失败: " + fileInfo.getTempFilePath(), e);
                }
            }

            // 清除会话记录
            tempFilesMap.remove(sessionId);

            // 删除临时目录
            try {
                Files.deleteIfExists(Paths.get(TEMP_DIR, sessionId));
            } catch (IOException e) {
                log.warn("删除临时目录失败: " + sessionId, e);
            }
        }

        return Result.success("临时文件已取消");
    }

    /**
     * 清理过期的临时文件
     */
    private static void cleanupExpiredTempFiles() {
        long now = System.currentTimeMillis();
        long expireTime = 2 * 60 * 60 * 1000; // 2小时

        tempFilesMap.forEach((sessionId, sessionFiles) -> {
            sessionFiles.entrySet().removeIf(entry -> {
                TempFileInfo fileInfo = entry.getValue();
                boolean expired = (now - fileInfo.getCreateTime()) > expireTime;

                if (expired) {
                    try {
                        Files.deleteIfExists(Paths.get(fileInfo.getTempFilePath()));
                    } catch (IOException e) {
                        log.warn("删除过期临时文件失败: " + fileInfo.getTempFilePath(), e);
                    }
                }

                return expired;
            });

            // 如果会话中的所有文件都已过期，移除会话记录
            if (sessionFiles.isEmpty()) {
                tempFilesMap.remove(sessionId);

                // 删除临时目录
                try {
                    Files.deleteIfExists(Paths.get(TEMP_DIR, sessionId));
                } catch (IOException e) {
                    log.warn("删除临时目录失败: " + sessionId, e);
                }
            }
        });
    }

    /**
     * 临时文件信息类
     */
    static class TempFileInfo {
        private String originalFilename;
        private String tempFilePath;
        private String targetDirPath;
        private String newFileName;
        private String fileType;
        private String fileUrl;
        private long createTime;

        // Getters and Setters
        public String getOriginalFilename() { return originalFilename; }
        public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

        public String getTempFilePath() { return tempFilePath; }
        public void setTempFilePath(String tempFilePath) { this.tempFilePath = tempFilePath; }

        public String getTargetDirPath() { return targetDirPath; }
        public void setTargetDirPath(String targetDirPath) { this.targetDirPath = targetDirPath; }

        public String getNewFileName() { return newFileName; }
        public void setNewFileName(String newFileName) { this.newFileName = newFileName; }

        public String getFileType() { return fileType; }
        public void setFileType(String fileType) { this.fileType = fileType; }

        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
    }
}