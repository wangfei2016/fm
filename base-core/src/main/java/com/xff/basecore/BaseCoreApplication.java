package com.xff.basecore;

import com.xff.basecore.common.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BaseCoreApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(BaseCoreApplication.class, args);
        SpringContextUtil.setApplicationContext(app);
    }

}
