package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotBlank(message = "用户邮箱不能为空")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
