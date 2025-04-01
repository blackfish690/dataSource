package com.sky.skydemo.dynamicdatasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DatabaseConnectionInterceptor implements HandlerInterceptor {

    private static final Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    private static final Object dataSourceCreationLock = new Object();

    // 用于数据源切换的锁
    private static final Object dataSourceSwitchLock = new Object();

    /**
     * 预处理请求，设置动态数据源并执行拦截逻辑。
     * <p>
     * 该方法在请求处理之前被调用，主要用于根据请求头中的租户ID设置动态数据源，
     * 并执行一些拦截逻辑（如日志记录等）。如果返回true，请求将继续处理；如果返回false，请求将被中断。
     *
     * @param request  HttpServletRequest对象，表示客户端的HTTP请求
     * @param response HttpServletResponse对象，表示服务器的HTTP响应
     * @param handler  处理请求的处理器对象
     * @return boolean 返回true表示继续处理请求，返回false表示中断请求处理
     * @throws Exception 如果在处理过程中发生异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 假设租户ID通过请求头传递
        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId != null) {
            if (tenantId.equals("sys")) {
                DruidDataSource druidDataSource = new DruidDataSource();
                druidDataSource.setUrl("jdbc:mysql://localhost:3306/sys?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
                druidDataSource.setUsername("root");
                druidDataSource.setPassword("password");
                configureDruidDataSource(druidDataSource);
                DynamicDataSource.dataSourcesMap.put(tenantId, druidDataSource);
                DynamicDataSource.setDataSource(tenantId);
            } else if (tenantId.equals("sys_test")) {
                DruidDataSource druidDataSource = new DruidDataSource();
                druidDataSource.setUrl("jdbc:mysql://localhost:3306/sys_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
                druidDataSource.setUsername("root");
                druidDataSource.setPassword("password");
                configureDruidDataSource(druidDataSource);
                DynamicDataSource.dataSourcesMap.put(tenantId, druidDataSource);
                DynamicDataSource.setDataSource(tenantId);
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * 在请求处理完成后执行清理操作。
     * 该方法用于清除当前线程中设置的数据源键，确保线程安全并避免数据源污染。
     *
     * @param request  HttpServletRequest对象，表示当前的HTTP请求
     * @param response HttpServletResponse对象，表示当前的HTTP响应
     * @param handler  处理当前请求的处理器对象
     * @param ex       在处理请求过程中抛出的异常，如果没有异常则为null
     * @throws Exception 如果在清理过程中发生错误，则抛出异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除当前线程的数据源
//        DynamicDataSourceContextHolder.clearDataSourceKey();
        DynamicDataSource.clear();
    }

    private DataSource getOrCreateDataSource(String tenantId) {
//        DataSource dataSource = dataSourceCache.get(tenantId);
//        if (dataSource == null) {
//            synchronized (dataSourceCreationLock) {
//                dataSource = dataSourceCache.get(tenantId);
//                if (dataSource == null) {
//                    dataSource = createDataSource(tenantId);
//                    if (dataSource != null) {
//                        dataSourceCache.put(tenantId, dataSource);
//                    }
//                }
//            }
//        }
//        return dataSource;
    }

    private void switchDataSource(String tenantId, DataSource dataSource) {
        synchronized (dataSourceSwitchLock) {
            DynamicDataSource dynamicDataSource = (DynamicDataSource) getDataSourceBean();
            Map<Object, Object> targetDataSources = new HashMap<>();
            targetDataSources.put(tenantId, dataSource);
            dynamicDataSource.setTargetDataSources(targetDataSources);
            dynamicDataSource.setDefaultTargetDataSource(dataSource);
            DynamicDataSourceContextHolder.setDataSourceKey(tenantId);
        }
    }


    private DataSource createDataSource(String tenantId) {
        DruidDataSource dataSource = new DruidDataSource();
        switch (tenantId) {
            case "sky":
                System.out.println("走了sky");
                dataSource.setUrl("jdbc:mysql://121.37.11.215:3306/sky-mall?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai");
                dataSource.setUsername("root");
                dataSource.setPassword("U2jbmZXTVK");
                break;
            case "sky-mall-test":
                System.out.println("走了test");
                dataSource.setUrl("jdbc:mysql://121.37.11.215:3306/sky-mall-test?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai");
                dataSource.setUsername("root");
                dataSource.setPassword("U2jbmZXTVK");
                break;
            default:
                return null;
        }
        return dataSource;
    }

    private DataSource getDataSourceBean() {
        // 获取DynamicDataSource Bean，这里可以使用ApplicationContext获取
        // 假设你已经在Spring配置中定义了DynamicDataSource Bean
        // 这里简化处理，实际使用时需要根据具体情况修改
        return new DynamicDataSource();
    }

    /**
     * 配置Druid数据源公共方法
     *
     * @param druidDataSource Druid数据源实例
     */
    private void configureDruidDataSource(DruidDataSource druidDataSource) {
        druidDataSource.setConnectionErrorRetryAttempts(3);
        druidDataSource.setTimeBetweenConnectErrorMillis(5000);
        druidDataSource.setBreakAfterAcquireFailure(true);
        druidDataSource.setMaxWait(10000);
        druidDataSource.setMaxActive(20);
    }
}