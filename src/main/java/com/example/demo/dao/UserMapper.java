package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    User selectById(@Param("id") Long id);

    List<User> selectAll();

    int updateById(User user);

    int deleteById(@Param("id") Long id);

    int existsById(@Param("id") Long id);
}
