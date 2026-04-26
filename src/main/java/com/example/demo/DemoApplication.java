package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot应用启动类
 *
 * @SpringBootApplication 是一个组合注解，包含以下三个注解的功能：
 * 1. @SpringBootConfiguration: 标识该类为配置类，等同于@Configuration
 * 2. @EnableAutoConfiguration: 启用Spring Boot自动配置
 * 3. @ComponentScan: 扫描当前包及其子包中的组件
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // 启动Spring Boot应用
        SpringApplication.run(DemoApplication.class, args);
    }
}