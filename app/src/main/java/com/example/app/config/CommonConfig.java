package com.example.app.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    ConfigService configService() {
        try {
            String serverAddr = "localhost:8848";
            return NacosFactory.createConfigService(serverAddr);
        } catch (NacosException e) {
            return null;
        }
    }
}
