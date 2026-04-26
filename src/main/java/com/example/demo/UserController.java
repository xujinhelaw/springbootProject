package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器类
 *
 * @RestController 注解标识该类为RESTful控制器
 * 相当于 @Controller + @ResponseBody 的组合
 *
 * @RequestMapping 指定该控制器处理的所有URL路径
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 注入UserService服务类
     * Spring会自动注入该依赖
     */
    @Autowired
    private UserService userService;

    /**
     * 创建用户接口
     *
     * @param requestData 请求体数据
     * @return 创建的用户对象
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> requestData) {
        String name = requestData.get("name");
        String email = requestData.get("email");

        try {
            User user = userService.createUser(name, email);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Map<String, Object> response = new HashMap<>();

        if (user == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return ResponseEntity.ok(response);
        }

        response.put("success", true);
        response.put("data", user);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", users);
        response.put("total", users.size());
        return ResponseEntity.ok(response);
    }

    /**
     * 更新用户信息
     *
     * @param id 用户ID
     * @param requestData 请求体数据
     * @return 更新后的用户对象
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestData) {
        String name = requestData.get("name");
        String email = requestData.get("email");

        User user = userService.updateUser(id, name, email);
        Map<String, Object> response = new HashMap<>();

        if (user == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return ResponseEntity.ok(response);
        }

        response.put("success", true);
        response.put("data", user);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);

        if (!deleted) {
            response.put("message", "用户不存在");
        } else {
            response.put("message", "删除成功");
        }

        return ResponseEntity.ok(response);
    }
}