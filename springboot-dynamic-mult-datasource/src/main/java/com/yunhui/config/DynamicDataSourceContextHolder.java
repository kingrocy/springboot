package com.yunhui.config;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {

    /**
     * 设置默认数据源的 key
     */
    private static final ThreadLocal<String> contextHolder = ThreadLocal.withInitial(() -> "default");

    /**
     * 数据源的 key 集合，用于切换时判断数据源是否存在
     */
    public static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * 获取当前数据源
     *
     * @return data source key
     */
    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    /**
     * 转换数据源
     *
     * @param key the key
     */
    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    /**
     * 将数据源设置为默认数据源
     */
    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    /**
     * 检查数据源是否在当前数据源列表
     *
     * @param key the key
     * @return boolean boolean
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }
}