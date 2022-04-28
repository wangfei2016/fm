package com.xff.basecore.decollat;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.Properties;

/**
 * MybatisStatementInterceptor.
 *
 * @author wang_fei
 * @since 2022/4/27 17:26
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
@Component
public class MybatisStatementInterceptor implements Interceptor {

    private static final ReflectorFactory defaultReflectorFactory = new DefaultReflectorFactory();
    public static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
    public static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                defaultReflectorFactory
        );

        MappedStatement mappedStatement = (MappedStatement)
                metaObject.getValue(DELEGATE_MAPPED_STATEMENT);

        Class<?> clazz = getTableClass(mappedStatement);
        if (clazz == null) {
            return invocation.proceed();
        }
        //获取表实体类上的注解
        TableShard tableShard = clazz.getAnnotation(TableShard.class);
        Table table = clazz.getAnnotation(Table.class);
        //如果注解存在就执行分表策略
        if (tableShard != null && table != null) {
            String tableName = table.name();
            Class<? extends TableShardStrategy> strategyClazz = tableShard.shardStrategy();
            TableShardStrategy strategy = strategyClazz.getDeclaredConstructor().newInstance();
            String tableShardName = strategy.getTableShardName(tableName);
            String sql = (String) metaObject.getValue(DELEGATE_BOUND_SQL_SQL);
            metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql.replaceAll(tableName, tableShardName.toUpperCase())); //替换表名
        }
        return invocation.proceed();
    }

    private Class<?> getTableClass(MappedStatement mappedStatement) throws ClassNotFoundException {
        String className = mappedStatement.getId();
        //获取BaseMapper的实现类
        className = className.substring(0, className.lastIndexOf('.'));
        Class<?> clazz = Class.forName(className);
        //返回BaseMapper泛型bean的Class
        if (BaseMapper.class.isAssignableFrom(clazz)) {
            return (Class<?>) ((ParameterizedType) (clazz.getGenericInterfaces()[0])).getActualTypeArguments()[0];
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

}

