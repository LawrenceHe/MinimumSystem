package com.example.demo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterReq {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
