package com.example.demo;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserController单元测试类
 *
 * 使用@ExtendWith(MockitoExtension.class)开启Mockito支持
 * 使用@Mock模拟依赖的Service类
 * 使用@InjectMocks注入被测试的Controller
 *
 * 这种测试方式不需要启动Spring容器,属于单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserController测试类")
class UserControllerTest {

    /**
     * 模拟UserService
     * 使用Mockito创建mock对象
     */
    @Mock
    private UserService userService;

    /**
     * 注入被测试的Controller
     * Mockito会自动将@Mock注入到@InjectMocks中
     */
    @InjectMocks
    private UserController userController;

    /**
     * 测试数据
     */
    private Map<String, String> requestData;
    private User testUser;

    /**
     * 初始化测试数据
     */
    @BeforeEach
    void setUp() {
        requestData = new HashMap<>();
        requestData.put("name", "测试用户");
        requestData.put("email", "test@example.com");

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("测试用户");
        testUser.setEmail("test@example.com");
    }

    /**
     * 测试createUser - 成功场景
     */
    @Test
    @DisplayName("创建用户-成功")
    void testCreateUser_Success() {
        // Mock设置: 当调用userService.createUser时,返回创建的用户
        when(userService.createUser("测试用户", "test@example.com")).thenReturn(testUser);

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.createUser(requestData);

        // 断言验证
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP状态码不匹配");
        assertNotNull(response.getBody(), "响应体不应为null");

        Boolean success = (Boolean) response.getBody().get("success");
        assertTrue(success, "success应为true");

        // 验证Mock被调用了一次
        verify(userService, times(1)).createUser("测试用户", "test@example.com");
    }

    /**
     * 测试createUser - 参数校验失败场景
     */
    @Test
    @DisplayName("创建用户-参数校验失败")
    void testCreateUser_ValidationFailed() {
        // 设置空数据
        Map<String, String> emptyData = new HashMap<>();
        emptyData.put("name", "");
        emptyData.put("email", "");

        // Mock设置: 抛出异常
        when(userService.createUser("", "")).thenThrow(new IllegalArgumentException("用户名不能为空"));

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.createUser(emptyData);

        // 断言验证
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP状态码应为400");
        assertNotNull(response.getBody(), "响应体不应为null");

        Boolean success = (Boolean) response.getBody().get("success");
        assertFalse(success, "success应为false");
    }

    /**
     * 测试getUser - 用户存在
     */
    @Test
    @DisplayName("根据ID获取用户-成功")
    void testGetUser_Success() {
        // Mock设置
        when(userService.getUserById(1L)).thenReturn(testUser);

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.getUser(1L);

        // 断言验证
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Boolean success = (Boolean) response.getBody().get("success");
        assertTrue(success, "success应为true");
    }

    /**
     * 测试getUser - 用户不存在
     */
    @Test
    @DisplayName("根据ID获取用户-不存在")
    void testGetUser_NotFound() {
        // Mock设置
        when(userService.getUserById(999L)).thenReturn(null);

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.getUser(999L);

        // 断言验证
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Boolean success = (Boolean) response.getBody().get("success");
        assertFalse(success, "success应为false");
    }

    /**
     * 测试getAllUsers - 成功
     */
    @Test
    @DisplayName("获取所有用户-成功")
    void testGetAllUsers_Success() {
        // Mock设置
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userService.getAllUsers()).thenReturn(userList);

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.getAllUsers();

        // 断言验证
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Boolean success = (Boolean) response.getBody().get("success");
        assertTrue(success, "success应为true");

        @SuppressWarnings("unchecked")
        List<User> data = (List<User>) response.getBody().get("data");
        assertEquals(1, data.size(), "用户数量应为1");
    }

    /**
     * 测试updateUser - 成功
     */
    @Test
    @DisplayName("更新用户-成功")
    void testUpdateUser_Success() {
        // Mock设置: 使用requestData中的实际值
        when(userService.updateUser(1L, "测试用户", "test@example.com"))
                .thenReturn(testUser);

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.updateUser(1L, requestData);

        // 断言验证
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Boolean success = (Boolean) response.getBody().get("success");
        assertTrue(success, "success应为true");
    }

    /**
     * 测试deleteUser - 成功
     */
    @Test
    @DisplayName("删除用户-成功")
    void testDeleteUser_Success() {
        // Mock设置
        when(userService.deleteUser(1L)).thenReturn(true);

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.deleteUser(1L);

        // 断言验证
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Boolean success = (Boolean) response.getBody().get("success");
        assertTrue(success, "success应为true");

        // 验证Mock被调用
        verify(userService, times(1)).deleteUser(1L);
    }

    /**
     * 测试deleteUser - 用户不存在
     */
    @Test
    @DisplayName("删除用户-不存在")
    void testDeleteUser_NotFound() {
        // Mock设置
        when(userService.deleteUser(999L)).thenReturn(false);

        // 执行测试
        ResponseEntity<Map<String, Object>> response = userController.deleteUser(999L);

        // 断言验证
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Boolean success = (Boolean) response.getBody().get("success");
        assertFalse(success, "success应为false");
    }
}