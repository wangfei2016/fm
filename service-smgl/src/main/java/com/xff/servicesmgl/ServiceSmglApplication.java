package com.xff.servicesmgl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.xff.servicesmgl.dao"})
@ComponentScan({"com.xff.servicesmgl", "com.xff.basecore.masterslave"})
public class ServiceSmglApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmglApplication.class, args);
    }

}
