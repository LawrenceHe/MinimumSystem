package com.example.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false )
@AllArgsConstructor
public class Role {
    private String code;
    private String name;
    private String description;
}
