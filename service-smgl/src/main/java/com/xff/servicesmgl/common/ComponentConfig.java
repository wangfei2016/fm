package com.xff.servicesmgl.common;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ComponentConfig.
 *
 * @author wang_fei
 * @since 2022/5/25 23:54
 */
@Configuration
@Import({com.xff.basecore.common.config.RedisConfig.class,
        com.xff.basecore.masterslave.DynamicDataSourceAspect.class,
        com.xff.basecore.decollat.MybatisStatementInterceptor.class,
        com.xff.basecore.common.util.ApplicationContextGetBeanHelper.class})
public class ComponentConfig {
}
