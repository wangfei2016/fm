///*
// * Copyright © 2021 https://www.cestc.cn/ All rights reserved.
// */
//package com.xff.basecore.decollat;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Plugin;
//import org.apache.ibatis.plugin.Signature;
//import org.apache.ibatis.reflection.DefaultReflectorFactory;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.ReflectorFactory;
//import org.apache.ibatis.reflection.SystemMetaObject;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.ParameterizedType;
//import java.sql.Connection;
//
///**
// * 请对类注释说明.
// *
// * @author wang_fei
// * @since 2022/4/27 17:26
// */
//@Intercepts({
//        @Signature(
//                type = StatementHandler.class,
//                method = "prepare",
//                args = {Connection.class, Integer.class}
//        )
//})
//@Slf4j
//@Component
//@ConditionalOnProperty(value = "table.shard.enabled", havingValue = "true") //加上了table.shard.enabled 该配置才会生效
//public class MybatisStatementInterceptor implements Interceptor {
//
//    private static final ReflectorFactory defaultReflectorFactory = new DefaultReflectorFactory();
//    public static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
//    public static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
//        MetaObject metaObject = MetaObject.forObject(statementHandler,
//                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
//                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
//                defaultReflectorFactory
//        );
//
//        MappedStatement mappedStatement = (MappedStatement)
//                metaObject.getValue(DELEGATE_MAPPED_STATEMENT);
//
//        Class<?> clazz = getTableClass(mappedStatement);
//        if (clazz == null) {
//            return invocation.proceed();
//        }
//        TableShard tableShard = clazz.getAnnotation(TableShard.class); //获取表实体类上的注解
//        Table table = clazz.getAnnotation(Table.class);
//        if (tableShard != null && table != null) { //如果注解存在就执行分表策略
//            String tableName = table.name();
//            Class<? extends TableShardStrategy> strategyClazz = tableShard.shardStrategy();
//            TableShardStrategy strategy = strategyClazz.getDeclaredConstructor().newInstance();
//            String tableShardName = strategy.getTableShardName(tableName);
//            String sql = (String) metaObject.getValue(DELEGATE_BOUND_SQL_SQL);
//            metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql.replaceAll(tableName, tableShardName.toUpperCase())); //替换表名
//        }
//        return invocation.proceed();
//    }
//
//    private Class<?> getTableClass(MappedStatement mappedStatement) throws ClassNotFoundException {
//        String className = mappedStatement.getId();
//        className = className.substring(0, className.lastIndexOf('.')); //获取到BaseMapper的实现类
//        Class<?> clazz = Class.forName(className);
//        if (BaseMapper.class.isAssignableFrom(clazz)) {
//            return (Class<?>) ((ParameterizedType) (clazz.getGenericInterfaces()[0])).getActualTypeArguments()[0]; //获取表实体类
//            //public interface XXXMapper extends BaseMapper<XXX> 其实就是获取到泛型中的具体表实体类
//            return null;
//        }
//
//        @Override
//        public Object plugin(Object target) {
//            if (target instanceof StatementHandler) {
//                return Plugin.wrap(target, this);
//            } else {
//                return target;
//            }
//        }
//
//    }
//
