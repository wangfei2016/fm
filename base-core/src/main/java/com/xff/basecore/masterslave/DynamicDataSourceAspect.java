package com.xff.basecore.masterslave;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * DynamicDataSourceAspect.
 *
 * @author wang_fei
 * @since 2022/3/24 11:03
 */
@Aspect
@Component
public class DynamicDataSourceAspect {

    private static final String[] QUERY_PREFIX = {"get", "query", "find", "select"};

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /**
     * Dao aspect.
     */
    @Pointcut("execution( * com.xff.*.dao.*.*(..))")
    public void daoAspect() {
    }

    /**
     * Switch DataSource
     *
     * @param point the point
     */
    @Before("daoAspect()")
    public void switchDataSource(JoinPoint point) {
        Boolean isQueryMethod = isQueryMethod(point.getSignature().getName());
        if (isQueryMethod) {
            DynamicDataSourceContextHolder.setDataSource(DataSourceKey.slave.name());
        } else {
            DynamicDataSourceContextHolder.setDataSource(DataSourceKey.master.name());
        }
        logger.info("Switch DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSource(), point.getSignature());
    }

    /**
     * Restore DataSource
     *
     * @param point the point
     */
    @After("daoAspect()")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clear();
        logger.info("Restore DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSource(), point.getSignature());
    }


    /**
     * Judge if method start with query prefix
     *
     * @param methodName
     * @return
     */
    private Boolean isQueryMethod(String methodName) {
        for (String prefix : QUERY_PREFIX) {
            if (methodName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

}
