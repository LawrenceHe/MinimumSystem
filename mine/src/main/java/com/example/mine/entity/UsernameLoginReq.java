package com.example.mine.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsernameLoginReq {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
