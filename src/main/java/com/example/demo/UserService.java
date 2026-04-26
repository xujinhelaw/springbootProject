package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户服务类
 *
 * @Service 注解将该类标记为Spring管理的服务层组件
 * 用于处理业务逻辑
 */
@Service
public class UserService {

    /**
     * 模拟数据存储-使用线程安全的Map
     */
    private final Map<Long, User> userStore = new ConcurrentHashMap<>();

    /**
     * 用户ID生成器
     */
    private Long idGenerator = 1L;

    /**
     * 创建用户
     *
     * @param name 用户名
     * @param email 用户邮箱
     * @return 创建后的用户对象
     */
    public User createUser(String name, String email) {
        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("用户邮箱不能为空");
        }

        // 创建用户对象
        User user = new User();
        user.setId(idGenerator++);
        user.setName(name);
        user.setEmail(email);

        // 存储用户
        userStore.put(user.getId(), user);

        return user;
    }

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象，如果不存在返回null
     */
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        return userStore.get(id);
    }

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(userStore.values());
    }

    /**
     * 更新用户信息
     *
     * @param id 用户ID
     * @param name 新用户名
     * @param email 新用户邮箱
     * @return 更新后的用户对象，如果用户不存在返回null
     */
    public User updateUser(Long id, String name, String email) {
        User existingUser = userStore.get(id);
        if (existingUser == null) {
            return null;
        }

        // 参数校验
        if (name != null && !name.trim().isEmpty()) {
            existingUser.setName(name);
        }
        if (email != null && !email.trim().isEmpty()) {
            existingUser.setEmail(email);
        }

        return existingUser;
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否删除成功
     */
    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }
        return userStore.remove(id) != null;
    }
}