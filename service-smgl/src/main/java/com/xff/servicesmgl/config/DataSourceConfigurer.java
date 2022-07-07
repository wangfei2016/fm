package com.xff.servicesmgl.config;

import com.xff.basecore.masterslave.DataSourceKey;
import com.xff.basecore.masterslave.DynamicRoutingDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSourceConfigurer.
 *
 * @author wang_fei
 * @since 2022/3/24 8:54
 */
@Configuration
public class DataSourceConfigurer {

    @Autowired
    private com.xff.basecore.decollat.MybatisStatementInterceptor mybatisStatementInterceptor;

    /**
     * master DataSource
     *
     * @return data source
     */
    @Bean("master")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource master() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Slave data source.
     *
     * @return the data source
     */
    @Bean("slave")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave")
    public DataSource slave() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Dynamic data source.
     *
     * @return the data source
     */
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("master") DataSource master, @Qualifier("slave") DataSource slave) {
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceKey.master.name(), master);
        targetDataSources.put(DataSourceKey.slave.name(), slave);
        //添加数据源
        dynamicDataSource.setTargetDataSources(targetDataSources);
        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(master);
        return dynamicDataSource;
    }

    /**
     * 配置多数据源导致下划线命名映射为驼峰失效，需要在下面sqlSessionFactoryBean方法中引入
     *
     * @return 配置
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration() {
        return new org.apache.ibatis.session.Configuration();
    }

    /**
     * Sql session factory bean.
     *
     * @return the sql session factory bean
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // Here to config mybatis
        sqlSessionFactoryBean.setTypeAliasesPackage("com.xff.servicesmgl.dao");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath" +
                ":mapper/*.xml"));
        // put all datasource into SqlSessionFactoryBean, then will autoconfig SqlSessionFactory
        sqlSessionFactoryBean.setDataSource(dynamicDataSource(master(), slave()));
        // 引入配置(下划线映射为驼峰等)
        sqlSessionFactoryBean.setConfiguration(globalConfiguration());
        // 引入拦截器插件(mybatisStatementInterceptor)
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{mybatisStatementInterceptor});
        return sqlSessionFactoryBean;
    }

}


