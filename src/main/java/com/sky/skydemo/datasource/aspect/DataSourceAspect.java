//
//package com.sky.skydemo.datasource.aspect;
//
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.fastjson.JSONObject;
//import com.sky.skydemo.datasource.annotation.SDataSource;
//import com.sky.skydemo.datasource.config.DynamicDataSource;
//import com.sky.skydemo.utils.RedisUtils;
//import org.activiti.engine.ProcessEngineConfiguration;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.sql.DataSource;
//import java.lang.reflect.Method;
//
///**
// * 多数据源，切面处理类
// *
// * @author Mark sunlightcs@gmail.com
// */
//@Aspect
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class DataSourceAspect {
//
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
//
//    @Autowired
//    private RedisUtils redisUtils;
//
//    protected Logger logger = LoggerFactory.getLogger(getClass());
//
//
//    @Pointcut("@within(io.epms.datasource.annotation.SDataSource)")
//    public void dataSourcePointCut() {
//
//    }
//
//    @Around("dataSourcePointCut()")
//    public Object around(ProceedingJoinPoint point) throws Throwable {
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        Class targetClass = point.getTarget().getClass();
//        Method method = signature.getMethod();
//
//        //// 获取RequestAttributes
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        // 从获取RequestAttributes中获取HttpServletRequest的信息
//        HttpServletRequest request = (HttpServletRequest) requestAttributes
//                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
//        //从header中获取token
//        String token = request.getHeader("Authorization");
//        //从Redis上获取律所信息
//        EpmCorpEntity epmCorpEntity = redisUtils.get(token + ":corp", EpmCorpEntity.class);
//        //获取律所连接
//        CorpDBConnForm corpDBConnForm = JSONObject.parseObject(epmCorpEntity.getDbconnstring(), CorpDBConnForm.class);
//
//        //从这里获取redis缓存的用户信息，通过该信息拼装数据库连接url
//
//        SDataSource targetDataSource = (SDataSource)targetClass.getAnnotation(SDataSource.class);
//        SDataSource methodDataSource = method.getAnnotation(SDataSource.class);
//        if(targetDataSource != null || methodDataSource != null){
//            //根据key向连接池获取连接，如果为空则创建连接并放进连接池,如果不为空就将连接池获取到的连接设置为当前连接
//            //律所ID作为连接的KEY
//            Object dbkey = DynamicDataSource.dataSourcesMap.get(epmCorpEntity.getCorpid());
//            if (dbkey == null){
//                DruidDataSource druidDataSource = new DruidDataSource();
//                druidDataSource.setUrl(corpDBConnForm.getServer()+corpDBConnForm.getDatabase());
//                druidDataSource.setUsername(corpDBConnForm.getUsername());
//                druidDataSource.setPassword(corpDBConnForm.getPassword());
//                druidDataSource.setConnectionErrorRetryAttempts(errorRetry);//连接错误后重试次数
//                druidDataSource.setTimeBetweenConnectErrorMillis(errorRetryInterval);//连接错误后重试时间
//                druidDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);//获取连接失败后再次获取
//                druidDataSource.setMaxWait(maxWait);//最大等待时间，单位毫秒
//                druidDataSource.setMaxActive(maxActive);//最大连接数
//                //将创建的连接放到连接池，律所ID作为连接的KEY
//                DynamicDataSource.dataSourcesMap.put(epmCorpEntity.getCorpid(), druidDataSource);
//                //设置当前连接
//                DynamicDataSource.setDataSource(epmCorpEntity.getCorpid());
//                //流程引擎连接
//                ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
//                processEngineConfiguration.setDataSource(druidDataSource);
//            }else{
//                DynamicDataSource.setDataSource(epmCorpEntity.getCorpid());
//                //流程引擎连接
//                ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
//                processEngineConfiguration.setDataSource((DataSource) DynamicDataSource.dataSourcesMap.get(epmCorpEntity.getCorpid()));
//            }
//        }
//
//        try {
//            return point.proceed();
//        } finally {
//            DynamicDataSource.clear();
//            logger.debug("clean datasource");
//        }
//    }
//}
