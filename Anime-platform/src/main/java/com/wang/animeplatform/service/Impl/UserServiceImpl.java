package com.wang.animeplatform.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.animeplatform.mapper.UserMapper;
import com.wang.animeplatform.pojo.LoginInfo;
import com.wang.animeplatform.pojo.PageResult;
import com.wang.animeplatform.pojo.Result;
import com.wang.animeplatform.pojo.User;
import com.wang.animeplatform.service.UserService;
import com.wang.animeplatform.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /*查询用户*/
    @Override
    public PageResult findAll(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Page<User> p = (Page<User>) userMapper.findAll();
        return new PageResult(p.getTotal(), p.getResult());
    }

    /*添加用户*/
    @Override
    public Result addUser(User user) {
        List<User> users = userMapper.findAll();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                log.info("用户名已存在");
                return Result.error("用户名已存在");
            } else if (u.getEmail().equals(user.getEmail())) {
                log.info("邮箱已存在");
                return Result.error("邮箱已存在");
            }
        }
        userMapper.addUser(user);
        return Result.success();
    }

    /*//    删除用户*/
    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public List<User> findUser(User user) {
        return userMapper.findUser();
    }

    @Override
    public User FindbyId(Integer id) {
        User user = userMapper.FindbyId(id);
        return user;
    }

    @Override
    public LoginInfo login(User user) {
        User userLogin = userMapper.getUsernameAndPasswordAndrole(user);
        if(userLogin != null){
            Map<String, Object> map = new HashMap<>();
            map.put("id", userLogin.getId());
            map.put("username", userLogin.getUsername());
            map.put("role", userLogin.getRole());
            String jwt = JwtUtils.generateJwt(map);
            LoginInfo loginInfo = new LoginInfo(userLogin.getId(), userLogin.getUsername(),userLogin.getRole() ,jwt);
            return loginInfo;
        }
        return null;

    }

    @Override
    public User FindbyToken(String token) {
        if(token != null){
            Map<String, Object> map = JwtUtils.parseJWT(token);
            Integer id = (Integer) map.get("id");
            return userMapper.FindbyId(id);
        }
        return null;
    }
}
