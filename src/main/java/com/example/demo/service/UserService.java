package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {

    User createUser(String name, String email);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, String name, String email);

    boolean deleteUser(Long id);
}
