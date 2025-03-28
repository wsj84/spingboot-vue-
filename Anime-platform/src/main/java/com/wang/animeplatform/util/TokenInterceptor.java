package com.wang.animeplatform.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 获取请求url。
        String url = request.getRequestURL().toString();

        //2. 判断请求url中是否包含login，如果包含，说明是登录操作，放行。
        if(url.contains("login")){ //登录请求
            log.info("登录请求 , 直接放行");
            return true;
        }
        if(url.contains("register")){ //观看页面
            log.info("查询");
            return true;
        }
        if(url.contains("/animes")){ //观看页面
            log.info("查询");
            return true;
        }
        if (url.contains("/image")) { // 图片请求
            log.info("图片请求, 直接放行");
            return true;
        }
        if (url.contains("/video") || url.contains("/upload") || url.contains("/")) { // 图片请求
            log.info("视频请求, 直接放行");
            return true;
        }
        //3. 获取请求头中的 Authorization。
        String authorizationHeader = request.getHeader("Authorization");

        //4. 判断 Authorization 是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(authorizationHeader)){ //Authorization 为空
            log.info("获取到 Authorization 为空, 返回错误结果");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //5. 提取 token，并去除 Bearer 前缀。
        String jwt = null;
        if (authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // 去除 "Bearer " 前缀
        } else {
            log.info("Authorization 格式不正确, 返回错误结果");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        System.out.println("获取到jwt令牌为:" + jwt);

        //6. 判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(jwt)){ //jwt为空
            log.info("获取到jwt令牌为空, 返回错误结果");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //7. 解析token，如果解析失败，返回错误结果（未登录）。
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败, 返回错误结果");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //8. 放行。
        log.info("令牌合法, 放行");
        return true;
    }
}
