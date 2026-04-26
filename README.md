# Spring Boot 基础开发模板

## 项目简介

一个基于 Spring Boot 2.7.18 的基础开发模板项目，提供完整的 CRUD 示例代码及 JUnit 单元测试。

## 技术栈

- Spring Boot 2.7.18
- JDK 1.8
- JUnit 5 + Mockito
- Maven

## 项目结构

```
springboot-project/
├── pom.xml                                    # Maven配置文件
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java         # Spring Boot启动类
│   │   │   ├── User.java                     # 用户实体类
│   │   │   ├── UserService.java              # 业务Service层
│   │   │   └── UserController.java           # REST控制器层
│   │   └── resources/
│   │       └── application.yml               # 应用配置文件
│   └── test/java/com/example/demo/
│       ├── UserServiceTest.java             # Service层单元测试
│       └── UserControllerTest.java          # Controller层单元测试
```

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/users | 创建用户 |
| GET | /api/users/{id} | 根据ID查询用户 |
| GET | /api/users | 获取所有用户 |
| PUT | /api/users/{id} | 更新用户 |
| DELETE | /api/users/{id} | 删除用户 |

## 快速开始

### 1. 运行项目

```bash
mvn spring-boot:run
```

访问地址: http://localhost:8080

### 2. 运行测试

```bash
mvn test
```

## 代码示例

### UserService 单元测试

```java
@DisplayName("UserService测试类")
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    @DisplayName("创建用户-正常场景")
    void testCreateUser_Success() {
        User user = userService.createUser("张三", "zhangsan@example.com");

        assertNotNull(user);
        assertEquals("张三", user.getName());
    }
}
```

### UserController 单元测试 (使用Mockito)

```java
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("创建用户-成功")
    void testCreateUser_Success() {
        when(userService.createUser("测试用户", "test@example.com"))
            .thenReturn(testUser);

        ResponseEntity<Map<String, Object>> response = userController.createUser(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

## 测试说明

- **UserServiceTest**: 纯单元测试，直接实例化 Service 类进行测试
- **UserControllerTest**: 使用 Mockito 模拟依赖，无需启动 Spring 容器

## 配置说明

```yaml
spring:
  application:
    name: springboot-demo

server:
  port: 8080

logging:
  level:
    root: INFO
    com.example.demo: DEBUG
```

## 扩展指南

### 添加新的Service层

1. 在 `src/main/java/com/example/demo/` 下创建 Service 类
2. 使用 `@Service` 注解标记
3. 编写对应的单元测试

### 添加新的Controller层

1. 在 `src/main/java/com/example/demo/` 下创建 Controller 类
2. 使用 `@RestController` 注解标记
3. 使用 `@Autowired` 注入依赖的 Service
4. 编写对应的单元测试(使用 Mockito)

## 许可证

MIT