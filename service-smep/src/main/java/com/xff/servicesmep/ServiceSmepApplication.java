package com.xff.servicesmep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.xff.servicesmep.dao"})
public class ServiceSmepApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmepApplication.class, args);
    }

}
