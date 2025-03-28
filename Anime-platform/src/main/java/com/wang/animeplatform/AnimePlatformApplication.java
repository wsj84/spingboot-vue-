package com.wang.animeplatform;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AnimePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimePlatformApplication.class, args);
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {

        @Value("${file.upload-dir}")
        private String uploadDir;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/image/**")
                    .addResourceLocations("file:" + uploadDir);
            registry.addResourceHandler("/video/**")
                    .addResourceLocations("file:G:\\Code\\毕设\\Anime-platform\\src\\main\\resources\\static\\video\\");
        }

    }
}
