package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User createUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("用户邮箱不能为空");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        userMapper.insert(user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    @Transactional
    public User updateUser(Long id, String name, String email) {
        if (id == null) {
            return null;
        }
        User existingUser = userMapper.selectById(id);
        if (existingUser == null) {
            return null;
        }

        if (name != null && !name.trim().isEmpty()) {
            existingUser.setName(name);
        }
        if (email != null && !email.trim().isEmpty()) {
            existingUser.setEmail(email);
        }

        userMapper.updateById(existingUser);
        return existingUser;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }
        int count = userMapper.existsById(id);
        if (count == 0) {
            return false;
        }
        userMapper.deleteById(id);
        return true;
    }
}
