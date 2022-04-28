package com.xff.servicesmgl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.xff.servicesmgl.dao"})
public class ServiceSmglApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmglApplication.class, args);
    }

}
