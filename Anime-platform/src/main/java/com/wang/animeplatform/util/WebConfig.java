package com.wang.animeplatform.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //拦截器对象
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       //注册自定义拦截器对象
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/index")
                .excludePathPatterns("/AnimeDetail")
                .excludePathPatterns("/animes");//设置不拦截的请求路径;
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 生产环境应限制为特定域名
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取操作系统名称
        String os = System.getProperty("os.name").toLowerCase();

        // 配置静态资源路径
        if (os.startsWith("win")) { // Windows 系统
            registry.addResourceHandler("/static/**") // 访问路径
                    .addResourceLocations("file:G:/Code/毕设/Anime-platform/src/main/resources/static/") // 本地文件路径
                    .addResourceLocations("classpath:/META-INF/resources/"); // Swagger UI 资源
        } else { // Linux 或 Mac 系统
            registry.addResourceHandler("/static/**") // 访问路径
                    .addResourceLocations("file:/Code/毕设/Anime-platform/src/main/resources/static/") // 本地文件路径
                    .addResourceLocations("classpath:/META-INF/resources/"); // Swagger UI 资源
        }
    }
}
