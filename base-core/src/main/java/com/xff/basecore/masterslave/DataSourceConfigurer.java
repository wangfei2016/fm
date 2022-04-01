package com.xff.basecore.masterslave;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
     * Sql session factory bean.
     *
     * @return the sql session factory bean
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // Here to config mybatis
        sqlSessionFactoryBean.setTypeAliasesPackage("com.xff.*.*.dao");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath" +
                ":mapper/*.xml"));
        // Here is very important, if don't config this, will can't switch datasource
        // put all datasource into SqlSessionFactoryBean, then will autoconfig SqlSessionFactory
        sqlSessionFactoryBean.setDataSource(dynamicDataSource(master(), slave()));
        return sqlSessionFactoryBean;
    }

}


