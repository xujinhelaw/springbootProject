# Spring Boot 基础开发模板

## 项目简介

一个基于 Spring Boot 2.7.18 的基础开发模板项目，采用标准的三层架构设计（Controller-Service-DAO），集成 MyBatis 和 MySQL 数据库持久化，提供完整的 CRUD 示例代码及单元测试。

## 技术栈

- Spring Boot 2.7.18
- JDK 1.8
- MyBatis 3.5.14
- MySQL 8.0.33
- JUnit 5 + Mockito
- Maven
- H2（测试数据库）

## 项目结构

```
springboot-project/
├── pom.xml                                    # Maven配置文件
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java         # Spring Boot启动类
│   │   │   ├── controller/
│   │   │   │   └── UserController.java      # REST控制器层
│   │   │   ├── service/
│   │   │   │   ├── UserService.java         # 服务接口
│   │   │   │   └── impl/
│   │   │   │       └── UserServiceImpl.java # 服务实现层
│   │   │   ├── dao/
│   │   │   │   └── UserMapper.java          # MyBatis数据访问层
│   │   │   ├── entity/
│   │   │   │   └── User.java                # 用户实体类
│   │   │   └── dto/
│   │   │       └── UserRequest.java         # 请求DTO类
│   │   └── resources/
│   │       ├── application.yml              # 应用配置文件
│   │       ├── mapper/
│   │       │   └── UserMapper.xml           # MyBatis SQL映射文件
│   │       └── sql/
│   │           └── init.sql                 # 数据库初始化脚本
│   └── test/
│       ├── java/com/example/demo/
│       │   ├── controller/
│       │   │   └── UserControllerTest.java  # Controller层单元测试
│       │   └── service/
│       │       └── impl/
│       │           └── UserServiceImplTest.java # Service层单元测试
│       └── resources/
│           ├── application.yml              # 测试环境配置
│           └── schema.sql                   # 测试数据库表结构
```

## API 接口

| 方法 | 路径 | 说明 | 请求体 |
|------|------|------|--------|
| POST | /api/users | 创建用户 | `{"name": "...", "email": "..."}` |
| GET | /api/users/{id} | 根据ID查询用户 | - |
| GET | /api/users | 获取所有用户 | - |
| PUT | /api/users/{id} | 更新用户 | `{"name": "...", "email": "..."}` |
| DELETE | /api/users/{id} | 删除用户 | - |

## 快速开始

### 1. 数据库准备

确保已安装 MySQL 8.0.33，并创建数据库：

```sql
CREATE DATABASE demo_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

然后执行初始化脚本 `src/main/resources/sql/init.sql` 创建表结构。

### 2. 修改数据库配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

### 3. 运行项目

```bash
mvn spring-boot:run
```

访问地址: http://localhost:8080

### 4. 运行测试

```bash
mvn test
```

测试使用 H2 内存数据库，无需额外配置。

## 架构说明

### 三层架构

- **Controller层** (`controller/`): 处理HTTP请求，参数校验，返回统一格式响应
- **Service层** (`service/`): 业务逻辑处理，事务控制
- **DAO层** (`dao/`): 数据访问，使用 MyBatis 操作数据库

### 统一响应格式

```json
{
  "success": true,
  "data": {},
  "message": "操作成功",
  "total": 10
}
```

## 配置说明

### 生产环境配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.demo.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### 测试环境配置

测试环境使用 H2 内存数据库，自动执行 `schema.sql` 建表：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=MySQL
    driver-class-name: org.h2.Driver
    username: sa
    password:
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql
```

## 扩展指南

### 添加新的业务模块

1. **实体类**: 在 `entity/` 包下创建实体类
2. **Mapper接口**: 在 `dao/` 包下创建 Mapper 接口，添加 `@Mapper` 注解
3. **Mapper XML**: 在 `resources/mapper/` 下创建对应的 SQL 映射文件
4. **Service接口**: 在 `service/` 包下创建 Service 接口
5. **Service实现**: 在 `service/impl/` 包下创建 Service 实现类，添加 `@Service` 注解
6. **Controller**: 在 `controller/` 包下创建 Controller 类，添加 `@RestController` 注解
7. **DTO**: 如有需要，在 `dto/` 包下创建请求/响应 DTO 类
8. **测试**: 编写对应的单元测试

## 许可证

MIT
