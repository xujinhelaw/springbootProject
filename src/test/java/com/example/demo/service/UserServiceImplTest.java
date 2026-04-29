package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserServiceImpl测试类")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("测试用户");
        testUser.setEmail("test@example.com");
    }

    @Nested
    @DisplayName("创建用户测试")
    class CreateUserTest {

        @Test
        @DisplayName("创建用户-成功")
        void testCreateUser_Success() {
            doAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return 1;
            }).when(userMapper).insert(any(User.class));

            User result = userService.createUser("张三", "zhangsan@example.com");

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("张三", result.getName());
            assertEquals("zhangsan@example.com", result.getEmail());
            verify(userMapper, times(1)).insert(any(User.class));
        }

        @Test
        @DisplayName("创建用户-用户名为空应抛出异常")
        void testCreateUser_NameIsBlank() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser("", "test@example.com")
            );
            verify(userMapper, never()).insert(any());
        }

        @Test
        @DisplayName("创建用户-用户名为null应抛出异常")
        void testCreateUser_NameIsNull() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser(null, "test@example.com")
            );
            verify(userMapper, never()).insert(any());
        }

        @Test
        @DisplayName("创建用户-邮箱为空应抛出异常")
        void testCreateUser_EmailIsBlank() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser("测试用户", "")
            );
            verify(userMapper, never()).insert(any());
        }

        @Test
        @DisplayName("创建用户-邮箱为null应抛出异常")
        void testCreateUser_EmailIsNull() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser("测试用户", null)
            );
            verify(userMapper, never()).insert(any());
        }
    }

    @Nested
    @DisplayName("根据ID查询用户测试")
    class GetUserByIdTest {

        @Test
        @DisplayName("查询存在的用户")
        void testGetUserById_Exists() {
            when(userMapper.selectById(1L)).thenReturn(testUser);

            User found = userService.getUserById(1L);

            assertNotNull(found);
            assertEquals(1L, found.getId());
            assertEquals("测试用户", found.getName());
            verify(userMapper, times(1)).selectById(1L);
        }

        @Test
        @DisplayName("查询不存在的用户应返回null")
        void testGetUserById_NotExists() {
            when(userMapper.selectById(999L)).thenReturn(null);

            User found = userService.getUserById(999L);

            assertNull(found);
            verify(userMapper, times(1)).selectById(999L);
        }

        @Test
        @DisplayName("查询id为null应返回null")
        void testGetUserById_IdIsNull() {
            User found = userService.getUserById(null);

            assertNull(found);
            verify(userMapper, never()).selectById(any());
        }
    }

    @Nested
    @DisplayName("获取所有用户测试")
    class GetAllUsersTest {

        @Test
        @DisplayName("无用户时返回空列表")
        void testGetAllUsers_Empty() {
            when(userMapper.selectAll()).thenReturn(Arrays.asList());

            List<User> users = userService.getAllUsers();

            assertNotNull(users);
            assertTrue(users.isEmpty());
            verify(userMapper, times(1)).selectAll();
        }

        @Test
        @DisplayName("有用户时返回正确数量")
        void testGetAllUsers_WithData() {
            User user2 = new User();
            user2.setId(2L);
            user2.setName("用户2");
            user2.setEmail("user2@example.com");

            when(userMapper.selectAll()).thenReturn(Arrays.asList(testUser, user2));

            List<User> users = userService.getAllUsers();

            assertEquals(2, users.size());
            verify(userMapper, times(1)).selectAll();
        }
    }

    @Nested
    @DisplayName("更新用户测试")
    class UpdateUserTest {

        @Test
        @DisplayName("更新存在的用户")
        void testUpdateUser_Success() {
            when(userMapper.selectById(1L)).thenReturn(testUser);
            when(userMapper.updateById(any(User.class))).thenReturn(1);

            User updated = userService.updateUser(1L, "新名称", "new@example.com");

            assertNotNull(updated);
            assertEquals("新名称", updated.getName());
            assertEquals("new@example.com", updated.getEmail());
            verify(userMapper, times(1)).updateById(any(User.class));
        }

        @Test
        @DisplayName("更新不存在的用户应返回null")
        void testUpdateUser_NotExists() {
            when(userMapper.selectById(999L)).thenReturn(null);

            User updated = userService.updateUser(999L, "新名称", "new@example.com");

            assertNull(updated);
            verify(userMapper, never()).updateById(any());
        }

        @Test
        @DisplayName("更新id为null应返回null")
        void testUpdateUser_IdIsNull() {
            User updated = userService.updateUser(null, "新名称", "new@example.com");

            assertNull(updated);
            verify(userMapper, never()).selectById(any());
        }

        @Test
        @DisplayName("部分更新-只更新name")
        void testUpdateUser_Partial_NameOnly() {
            when(userMapper.selectById(1L)).thenReturn(testUser);
            when(userMapper.updateById(any(User.class))).thenReturn(1);

            User updated = userService.updateUser(1L, "新名称", null);

            assertNotNull(updated);
            assertEquals("新名称", updated.getName());
            assertEquals("test@example.com", updated.getEmail());
        }

        @Test
        @DisplayName("部分更新-只更新email")
        void testUpdateUser_Partial_EmailOnly() {
            when(userMapper.selectById(1L)).thenReturn(testUser);
            when(userMapper.updateById(any(User.class))).thenReturn(1);

            User updated = userService.updateUser(1L, null, "new@example.com");

            assertNotNull(updated);
            assertEquals("测试用户", updated.getName());
            assertEquals("new@example.com", updated.getEmail());
        }
    }

    @Nested
    @DisplayName("删除用户测试")
    class DeleteUserTest {

        @Test
        @DisplayName("删除存在的用户")
        void testDeleteUser_Success() {
            when(userMapper.existsById(1L)).thenReturn(1);
            when(userMapper.deleteById(1L)).thenReturn(1);

            boolean deleted = userService.deleteUser(1L);

            assertTrue(deleted);
            verify(userMapper, times(1)).existsById(1L);
            verify(userMapper, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("删除不存在的用户应返回false")
        void testDeleteUser_NotExists() {
            when(userMapper.existsById(999L)).thenReturn(0);

            boolean deleted = userService.deleteUser(999L);

            assertFalse(deleted);
            verify(userMapper, times(1)).existsById(999L);
            verify(userMapper, never()).deleteById(any());
        }

        @Test
        @DisplayName("删除id为null应返回false")
        void testDeleteUser_IdIsNull() {
            boolean deleted = userService.deleteUser(null);

            assertFalse(deleted);
            verify(userMapper, never()).existsById(any());
            verify(userMapper, never()).deleteById(any());
        }
    }
}
