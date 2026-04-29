package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController测试类")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequest requestData;
    private User testUser;

    @BeforeEach
    void setUp() {
        requestData = new UserRequest();
        requestData.setName("测试用户");
        requestData.setEmail("test@example.com");

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("测试用户");
        testUser.setEmail("test@example.com");
    }

    @Test
    @DisplayName("创建用户-成功")
    void testCreateUser_Success() {
        when(userService.createUser("测试用户", "test@example.com")).thenReturn(testUser);

        ResponseEntity<?> response = userController.createUser(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).createUser("测试用户", "test@example.com");
    }

    @Test
    @DisplayName("获取用户-成功")
    void testGetUser_Success() {
        when(userService.getUserById(1L)).thenReturn(testUser);

        ResponseEntity<?> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("获取用户-不存在")
    void testGetUser_NotFound() {
        when(userService.getUserById(999L)).thenReturn(null);

        ResponseEntity<?> response = userController.getUser(999L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("获取所有用户-成功")
    void testGetAllUsers_Success() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<?> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("更新用户-成功")
    void testUpdateUser_Success() {
        when(userService.updateUser(1L, "测试用户", "test@example.com")).thenReturn(testUser);

        ResponseEntity<?> response = userController.updateUser(1L, requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("删除用户-成功")
    void testDeleteUser_Success() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("删除用户-不存在")
    void testDeleteUser_NotFound() {
        when(userService.deleteUser(999L)).thenReturn(false);

        ResponseEntity<?> response = userController.deleteUser(999L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
