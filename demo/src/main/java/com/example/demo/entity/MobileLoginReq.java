package com.example.demo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MobileLoginReq {

    @NotBlank
    @Pattern(regexp="^1\\d{10}")
    private String mobile;

    @NotBlank
    private String messageToken;
}
