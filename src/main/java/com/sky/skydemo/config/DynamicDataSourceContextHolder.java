package com.sky.skydemo.config;

public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    public static void clearDataSourceKey() {
        contextHolder.remove();
    }
}