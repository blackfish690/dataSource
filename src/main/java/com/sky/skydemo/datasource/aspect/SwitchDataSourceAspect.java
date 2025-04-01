//package com.sky.skydemo.datasource.aspect;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.sky.skydemo.datasource.annotation.SwitchDataSource;
//import com.sky.skydemo.datasource.config.DynamicDataSource;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 多数据源，切面处理类
// *
// * @author
// */
//@Aspect
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class SwitchDataSourceAspect {
//
//    /**
//     * 日志记录器
//     */
//    protected Logger logger = LoggerFactory.getLogger(getClass());
//
//    /**
//     * 数据源配置参数
//     */
//    @Value("${prop.druidDataSourceConfig.error-retry}")
//    private Integer errorRetry;
//    @Value("${prop.druidDataSourceConfig.error-retry-interval}")
//    private Integer errorRetryInterval;
//    @Value("${prop.druidDataSourceConfig.breakafteracquirefailure}")
//    private Boolean breakAfterAcquireFailure;
//    @Value("${prop.druidDataSourceConfig.maxwait}")
//    private Integer maxWait;
//    @Value("${prop.druidDataSourceConfig.maxactive}")
//    private Integer maxActive;
//
//    /**
//     * 切入点：处理带有SwitchDataSource注解的类
//     */
//    @Pointcut("@within(com.sky.skydemo.datasource.annotation.SwitchDataSource)")
//    public void dataSourcePointCut() {
//    }
//
//    /**
//     * 切入点：处理带有SwitchDataSource注解的方法
//     */
//    @Pointcut("@annotation(com.sky.skydemo.datasource.annotation.SwitchDataSource) ")
//    public void dataSourcePointCutP() {
//    }
//
//    /**
//     * 环绕通知：处理带有SwitchDataSource注解的方法
//     *
//     * @param point 切入点
//     * @return 方法执行结果
//     * @throws Throwable 方法执行异常
//     */
//    @Around("dataSourcePointCutP()")
//    public Object aroundP(ProceedingJoinPoint point) throws Throwable {
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        Class targetClass = point.getTarget().getClass();
//        Method method = signature.getMethod();
//
//        // 获取请求头
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = (HttpServletRequest) requestAttributes
//                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
//        String authorization = request.getHeader("Authorization");
//
//        if (!"".equalsIgnoreCase(authorization)) {
//            SwitchDataSource targetDataSource = (SwitchDataSource) targetClass.getAnnotation(SwitchDataSource.class);
//            SwitchDataSource methodDataSource = method.getAnnotation(SwitchDataSource.class);
//
//            if (targetDataSource != null || methodDataSource != null) {
//                // 根据请求头中的Authorization信息选择数据源
//                if (authorization.equals("sys")) {
//                    // 系统测试数据库配置
//                    DruidDataSource druidDataSource = new DruidDataSource();
//                    druidDataSource.setUrl("jdbc:mysql://localhost:3306/sys_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
//                    druidDataSource.setUsername("root");
//                    druidDataSource.setPassword("password");
//                    configureDruidDataSource(druidDataSource);
//                    DynamicDataSource.dataSourcesMap.put(authorization, druidDataSource);
//                    DynamicDataSource.setDataSource(authorization);
//                } else {
//                    // 默认数据库配置
//                    DruidDataSource druidDataSource = new DruidDataSource();
//                    druidDataSource.setUrl("jdbc:mysql://localhost:3306/sys?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
//                    druidDataSource.setUsername("root");
//                    druidDataSource.setPassword("password");
//                    configureDruidDataSource(druidDataSource);
//                    DynamicDataSource.dataSourcesMap.put(authorization, druidDataSource);
//                    DynamicDataSource.setDataSource(authorization);
//                }
//            }
//        }
//
//        // 尝试执行某个操作，并处理可能发生的异常
//        try {
//            // 执行操作，point.proceed()表示执行当前切面的下一个方法
//            return point.proceed();
//        } catch (Exception e) {
//            // 捕获异常，记录错误日志，并返回null
//            logger.error(e.toString());
//            return null;
//        } finally {
//            // 无论是否发生异常，都执行清理操作
//            DynamicDataSource.clear();
//            logger.debug("clean datasource");
//        }
//    }
//
//    /**
//     * 配置Druid数据源公共方法
//     *
//     * @param druidDataSource Druid数据源实例
//     */
//    private void configureDruidDataSource(DruidDataSource druidDataSource) {
//        druidDataSource.setConnectionErrorRetryAttempts(errorRetry);
//        druidDataSource.setTimeBetweenConnectErrorMillis(errorRetryInterval);
//        druidDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
//        druidDataSource.setMaxWait(maxWait);
//        druidDataSource.setMaxActive(maxActive);
//    }
//}
