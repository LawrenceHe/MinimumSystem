package com.example.mine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ("com.example.mine"))
public class MineApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MineApplication.class, args);
    }

}
