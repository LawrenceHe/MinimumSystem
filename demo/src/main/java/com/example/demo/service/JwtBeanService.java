package com.example.demo.service;

import com.example.demo.util.JwtObject;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
public class JwtBeanService {

    @Autowired
    private JwtObject jwtObject;
}
