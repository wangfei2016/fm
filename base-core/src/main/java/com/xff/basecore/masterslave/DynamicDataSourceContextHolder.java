package com.xff.basecore.masterslave;

import org.apache.commons.lang.StringUtils;

/**
 * DynamicDataSourceContextHolder.
 *
 * @author wang_fei
 * @since 2022/3/24 9:08
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new InheritableThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param db
     */
    public static void setDataSource(String db) {
        contextHolder.set(db);
    }

    /**
     * 取得当前数据源
     *
     * @return
     */
    public static String getDataSource() {
        String strDataSource = contextHolder.get();
        return StringUtils.isBlank(strDataSource) ? "default" : strDataSource;
    }

    /**
     * 清除上下文数据
     */
    public static void clear() {
        contextHolder.remove();
    }

}

