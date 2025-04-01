package com.sky.skydemo.dynamicdatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> dataSourceKey = ThreadLocal.withInitial(() -> "defaultDataSource");

    // 存储数据源的映射，使用ConcurrentHashMap保证线程安全
    public static Map<Object, Object> dataSourcesMap = new ConcurrentHashMap<>(100);

    // 静态初始化块，初始化默认数据源
//    static {
//        dataSourcesMap.put("defaultDataSource", SpringUtils.getBean("defaultDataSource"));
//    }

    /**
     * 重写determineCurrentLookupKey方法，获取当前线程的数据源键值
     * @return 当前线程的数据源键值
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSource.dataSourceKey.get();
    }

    /**
     * 设置当前线程的数据源键值
     * @param dataSource 数据源键值
     */
    public static void setDataSource(String dataSource) {
        DynamicDataSource.dataSourceKey.set(dataSource);
        DynamicDataSource dynamicDataSource = (DynamicDataSource) SpringUtils.getBean("dataSource");
        dynamicDataSource.afterPropertiesSet();
    }

    /**
     * 获取当前线程的数据源键值
     * @return 当前线程的数据源键值
     */
    public static String getDataSource() {
        return DynamicDataSource.dataSourceKey.get();
    }

    /**
     * 清除当前线程的数据源键值
     */
    public static void clear() {
        DynamicDataSource.dataSourceKey.remove();
    }
}