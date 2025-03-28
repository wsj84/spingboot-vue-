package com.wang.animeplatform.mapper;

import com.wang.animeplatform.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserMapper {
     List<User> findAll();

    void addUser(User user);

    void deleteUser(Integer id);

    void updateUser(User user);

    List<User> findUser();

    User FindbyId(Integer id);

    User getUsernameAndPasswordAndrole(User user);
}
