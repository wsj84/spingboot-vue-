package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.LoginInfo;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.pojo.User;
import com.wang.animeplatform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("员工来登录啦 , {}", user);
        LoginInfo loginInfo = userService.login(user);
        log.info("员工信息 , {}", loginInfo);
        if (loginInfo != null) {
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误~");
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        log.info("员工来注册啦 , {}", user);
        Result msg = userService.addUser(user);
        log.info("员工信息 , {}", msg);
        if (msg.getCode() != 0) {
            return Result.success();
        }
        return msg;
    }

}
