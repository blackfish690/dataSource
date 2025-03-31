package com.sky.skydemo.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源配置类，继承自AbstractRoutingDataSource
 * 用于在运行时切换数据源，支持多数据源配置
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    // 日志记录器
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    // Druid数据源配置属性
    @Value("${prop.druidDataSourceConfig.error-retry}")
    private Integer errorRetry;
    @Value("${prop.druidDataSourceConfig.error-retry-interval}")
    private Integer errorRetryInterval;
    @Value("${prop.druidDataSourceConfig.breakafteracquirefailure}")
    private Boolean breakAfterAcquireFailure;
    @Value("${prop.druidDataSourceConfig.maxwait}")
    private Integer maxWait;
    @Value("${prop.druidDataSourceConfig.maxactive}")
    private Integer maxActive;

    // Redis工具类，用于与Redis交互
//    @Autowired
//    private RedisUtils redisUtils;

    /**
     * 定义一个线程本地变量来存储数据源的键值，默认值为"defaultDataSource"
     * 使用ThreadLocal确保每个线程都有自己独立的数据源键值副本，避免线程安全问题
     */
    private static final ThreadLocal<String> dataSourceKey = ThreadLocal.withInitial(() -> "defaultDataSource");

    // 存储数据源的映射，使用ConcurrentHashMap保证线程安全
    public static Map<Object, Object> dataSourcesMap = new ConcurrentHashMap<>(100);

    // 静态初始化块，初始化默认数据源
    static {
        dataSourcesMap.put("defaultDataSource", SpringUtils.getBean("defaultDataSource"));
    }

    /**
     * 重写determineCurrentLookupKey方法，获取当前线程的数据源键值
     * @return 当前线程的数据源键值
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSource.dataSourceKey.get();
    }

    /**
     * 重写determineTargetDataSource方法，根据数据源键值确定目标数据源
     * @return 目标数据源
     */
    @Override
    protected DataSource determineTargetDataSource() {
        Object lookupKey = determineCurrentLookupKey();
        DataSource dataSource = (DataSource) DynamicDataSource.dataSourcesMap.get(lookupKey.toString());
        if (dataSource == null && lookupKey == null) {
            dataSource = (DataSource) DynamicDataSource.dataSourcesMap.get("defaultDataSource");
        }
        if (dataSource == null) {
            // 获取RequestAttributes
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            // 从获取RequestAttributes中获取HttpServletRequest的信息
            HttpServletRequest request = (HttpServletRequest) requestAttributes
                    .resolveReference(RequestAttributes.REFERENCE_REQUEST);
            //从header中获取token
            String token = request.getHeader("Authorization");
            //从Redis上获取律所信息
//            EpmCorpEntity epmCorpEntity = redisUtils.get(token + ":corp", EpmCorpEntity.class);
//            //获取律所连接
//            CorpDBConnForm corpDBConnForm = JSONObject.parseObject(epmCorpEntity.getDbconnstring(), CorpDBConnForm.class);

            // 创建Druid数据源并配置属性
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl("jdbc:mysql://localhost:3306/sys?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
            druidDataSource.setUsername("root");
            druidDataSource.setPassword("password");
            druidDataSource.setConnectionErrorRetryAttempts(errorRetry);//连接错误后重试次数
            druidDataSource.setTimeBetweenConnectErrorMillis(errorRetryInterval);//连接错误后重试时间
            druidDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);//获取连接失败后再次获取
            druidDataSource.setMaxWait(maxWait);//最大等待时间，单位毫秒
            druidDataSource.setMaxActive(maxActive);//最大连接数
            //将创建的连接放到连接池，律所ID作为连接的KEY
            DynamicDataSource.dataSourcesMap.put(lookupKey, druidDataSource);

            dataSource = (DataSource) DynamicDataSource.dataSourcesMap.get(lookupKey);
//            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
            logger.error("-------------------------------------------------找不到连接---------------------------------------------");
        }
        return dataSource;
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
