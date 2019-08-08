package com.example.usercenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false )
@AllArgsConstructor
public class Role {
    private String code;
    private String name;
    private String description;
}
