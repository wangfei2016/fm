package com.xff.servicesmgl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.xff.mybatistest.*.dao"})
public class ServiceSmglApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmglApplication.class, args);
    }

}
