package com.wang.animeplatform.service;

import com.wang.animeplatform.pojo.LoginInfo;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.pojo.User;


import java.util.List;

public interface UserService {

    PageResult findAll(Integer page, Integer pageSize );

    Result addUser(User user);

    void deleteUser(Integer id);

    void updateUser(User user);

    List<User> findUser(User user);

    User FindbyId(Integer id);

    LoginInfo login(User user);

    User FindbyToken(String token);
}
