package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserService单元测试类
 *
 * 使用JUnit 5 (Junit Jupiter)进行单元测试
 * Spring Boot Starter Test已包含JUnit 5和Mockito依赖
 *
 * 测试类的命名规范: 被测试类名 + Test
 * 测试方法的命名规范: test + 被测试方法名 或 使用@DisplayName描述
 */
@DisplayName("UserService测试类")
class UserServiceTest {

    /**
     * 待测试的服务类实例
     */
    private UserService userService;

    /**
     * 每个测试方法执行前初始化
     * 相当于JUnit 4的@Before
     */
    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    /**
     * 测试createUser方法 - 正常场景
     */
    @Test
    @DisplayName("创建用户-正常场景")
    void testCreateUser_Success() {
        // 执行测试
        User user = userService.createUser("张三", "zhangsan@example.com");

        // 断言验证
        assertNotNull(user, "创建的用户不应为null");
        assertNotNull(user.getId(), "用户ID不应为null");
        assertEquals("张三", user.getName(), "用户名不匹配");
        assertEquals("zhangsan@example.com", user.getEmail(), "用户邮箱不匹配");
    }

    /**
     * 测试createUser方法 - 用户名为空
     */
    @Test
    @DisplayName("创建用户-用户名为空应抛出异常")
    void testCreateUser_NameIsBlank() {
        // 断言异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("", "test@example.com")
        );

        assertEquals("用户名不能为空", exception.getMessage(), "异常消息不匹配");
    }

    /**
     * 测试createUser方法 - 用户名为null
     */
    @Test
    @DisplayName("创建用户-用户名为null应抛出异常")
    void testCreateUser_NameIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(null, "test@example.com")
        );
    }

    /**
     * 测试createUser方法 - 邮箱为空
     */
    @Test
    @DisplayName("创建用户-邮箱为空应抛出异常")
    void testCreateUser_EmailIsBlank() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("测试用户", "")
        );
    }

    /**
     * 测试createUser方法 - 邮箱为null
     */
    @Test
    @DisplayName("创建用户-邮箱为null应抛出异常")
    void testCreateUser_EmailIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("测试用户", null)
        );
    }

    /**
     * 测试getUserById方法
     */
    @Nested
    @DisplayName("根据ID查询用户测试")
    class GetUserByIdTest {

        @Test
        @DisplayName("查询存在的用户")
        void testGetUserById_Exists() {
            // 先创建一个用户
            User createdUser = userService.createUser("李四", "lisi@example.com");

            // 查询该用户
            User foundUser = userService.getUserById(createdUser.getId());

            // 断言
            assertNotNull(foundUser, "应能查到用户");
            assertEquals(createdUser.getId(), foundUser.getId(), "用户ID不匹配");
            assertEquals("李四", foundUser.getName(), "用户名不匹配");
        }

        @Test
        @DisplayName("查询不存在的用户应返回null")
        void testGetUserById_NotExists() {
            User user = userService.getUserById(999L);
            assertNull(user, "不存在的用户应返回null");
        }

        @Test
        @DisplayName("查询id为null应返回null")
        void testGetUserById_IdIsNull() {
            User user = userService.getUserById(null);
            assertNull(user, "id为null应返回null");
        }
    }

    /**
     * 测试getAllUsers方法
     */
    @Nested
    @DisplayName("获取所有用户测试")
    class GetAllUsersTest {

        @Test
        @DisplayName("无用户时返回空列表")
        void testGetAllUsers_Empty() {
            List<User> users = userService.getAllUsers();
            assertNotNull(users, "返回列表不应为null");
            assertTrue(users.isEmpty(), "初始用户列表应为空");
        }

        @Test
        @DisplayName("有用户时返回正确数量")
        void testGetAllUsers_WithData() {
            // 创建多个用户
            userService.createUser("用户1", "user1@example.com");
            userService.createUser("用户2", "user2@example.com");
            userService.createUser("用户3", "user3@example.com");

            List<User> users = userService.getAllUsers();

            assertEquals(3, users.size(), "用户数量不匹配");
        }
    }

    /**
     * 测试updateUser方法
     */
    @Nested
    @DisplayName("更新用户测试")
    class UpdateUserTest {

        @Test
        @DisplayName("更新存在的用户")
        void testUpdateUser_Success() {
            // 先创建用户
            User createdUser = userService.createUser("原始名称", "original@example.com");

            // 更新用户
            User updatedUser = userService.updateUser(
                    createdUser.getId(),
                    "新名称",
                    "new@example.com"
            );

            assertNotNull(updatedUser, "更新后的用户不应为null");
            assertEquals("新名称", updatedUser.getName(), "用户名未更新");
            assertEquals("new@example.com", updatedUser.getEmail(), "用户邮箱未更新");
        }

        @Test
        @DisplayName("更新不存在的用户")
        void testUpdateUser_NotExists() {
            User user = userService.updateUser(999L, "新名称", "new@example.com");
            assertNull(user, "不存在的用户应返回null");
        }
    }

    /**
     * 测试deleteUser方法
     */
    @Nested
    @DisplayName("删除用户测试")
    class DeleteUserTest {

        @Test
        @DisplayName("删除存在的用户")
        void testDeleteUser_Success() {
            // 先创建用户
            User createdUser = userService.createUser("待删除", "delete@example.com");
            Long userId = createdUser.getId();

            // 删除用户
            boolean deleted = userService.deleteUser(userId);

            // 断言
            assertTrue(deleted, "删除应返回true");
            assertNull(userService.getUserById(userId), "删除后应无法查到");
        }

        @Test
        @DisplayName("删除不存在的用户")
        void testDeleteUser_NotExists() {
            boolean deleted = userService.deleteUser(999L);
            assertFalse(deleted, "删除不存在的用户应返回false");
        }

        @Test
        @DisplayName("删除id为null")
        void testDeleteUser_IdIsNull() {
            boolean deleted = userService.deleteUser(null);
            assertFalse(deleted, "id为null应返回false");
        }
    }
}