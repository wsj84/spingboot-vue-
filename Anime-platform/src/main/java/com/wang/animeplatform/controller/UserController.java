package com.wang.animeplatform.controller;

import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.pojo.User;
import com.wang.animeplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // 添加了通用的URL路径前缀，所有请求路径都以/users开头
public class UserController {
    @Autowired
    UserService userService;
    /*查询需求用户*/
    @GetMapping // 添加了具体的URL路径
    public Result FindAll(@RequestParam(defaultValue = "1") Integer page ,
                          @RequestParam(defaultValue = "10") Integer pageSize ) {

        PageResult pageResult = userService.findAll(page, pageSize);
        System.out.println(pageResult);
        return Result.success(pageResult);  // 将查询到的用户列表作为参数传递给Result.success()
    }
   /* 根据id查询用户*/
    @GetMapping("/{id}")
    public Result FindbyId(@PathVariable Integer id) {
        User user = userService.FindbyId(id);
        return Result.success(user);
    }

    /*根据token来查询用户*/
    @GetMapping("/token")
    public Result FindbyToken(String token) {
        User user = userService.FindbyToken(token);
        return Result.success(user);
    }
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }
    /*注册用户*/
    @PostMapping
    public Result addUser(@RequestBody User user) {

        return userService.addUser(user);
    }
    /*更新用户信息*/
    @PutMapping
    public Result updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }
}