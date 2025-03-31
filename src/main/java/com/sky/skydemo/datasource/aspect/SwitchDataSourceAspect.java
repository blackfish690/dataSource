package com.sky.skydemo.datasource.aspect;

import com.alibaba.druid.pool.DruidDataSource;
import com.sky.skydemo.datasource.annotation.SwitchDataSource;
import com.sky.skydemo.datasource.config.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源，切面处理类
 *
 * @author
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SwitchDataSourceAspect {


    protected Logger logger = LoggerFactory.getLogger(getClass());

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


    @Pointcut("@within(com.sky.skydemo.datasource.annotation.SwitchDataSource)")
    public void dataSourcePointCut() {
    }

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
//        SwitchDataSource targetDataSource = (SwitchDataSource)targetClass.getAnnotation(SwitchDataSource.class);
//        SwitchDataSource methodDataSource = method.getAnnotation(SwitchDataSource.class);
//        if(targetDataSource != null || methodDataSource != null){
//            //根据key向连接池获取连接，如果为空则创建连接并放进连接池,如果不为空就将连接池获取到的连接设置为当前连接
//            //律所ID作为连接的KEY
//            Object dbkey = DynamicDataSource.dataSourcesMap.get(epmCorpEntity.getCorpid());
//            if (dbkey == null){
//                DruidDataSource druidDataSource = new DruidDataSource();
//                druidDataSource.setUrl(corpDBConnForm.getServer()+corpDBConnForm.getDatabase());
//                druidDataSource.setUsername(corpDBConnForm.getUsername());
//                druidDataSource.setPassword(corpDBConnForm.getPassword());
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
    @Pointcut("@annotation(com.sky.skydemo.datasource.annotation.SwitchDataSource) ")
    public void dataSourcePointCutP() {
    }

//    @Autowired
//    private CloseableHttpClientToMaster closeableHttpClientToMaster;

    @Around("dataSourcePointCutP()")
    public Object aroundP(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();

        //判断如果有corpId的话，就去master获取对应corp的连接字符串，如果没有则回到从缓存获取
        String corpId = "";
        //请求的参数
        Object[] args = point.getArgs();
        String[] argsNames = signature.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < Math.max(argsNames.length, args.length); i++) {
            //参数带有corpId时进行处理
            if ("corpId".equalsIgnoreCase(argsNames[i])) {
                if (args[i] != null) {
                    corpId = args[i].toString();
                }
            }
        }

        logger.debug("注解：指定多数据源请求参数(corpId)：" + corpId);

        //获取请求头
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String authorization = request.getHeader("Authorization");

        if (!"".equalsIgnoreCase(authorization)) {
            SwitchDataSource targetDataSource = (SwitchDataSource) targetClass.getAnnotation(SwitchDataSource.class);
            SwitchDataSource methodDataSource = method.getAnnotation(SwitchDataSource.class);

            if (targetDataSource != null || methodDataSource != null) {
                //此处可以修改为使用redis或缓存获取,还可以使用http客户端获取
                if (authorization.equals("sys")) {
                    DruidDataSource druidDataSource = new DruidDataSource();
                    druidDataSource.setUrl("jdbc:mysql://localhost:3306/sys_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
                    druidDataSource.setUsername("root");
                    druidDataSource.setPassword("password");
                    druidDataSource.setConnectionErrorRetryAttempts(errorRetry);//连接错误后重试次数
                    druidDataSource.setTimeBetweenConnectErrorMillis(errorRetryInterval);//连接错误后重试时间
                    druidDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);//获取连接失败后再次获取
                    druidDataSource.setMaxWait(maxWait);//最大等待时间，单位毫秒
                    druidDataSource.setMaxActive(maxActive);//最大连接数
                    //将创建的连接放到连接池，律所ID作为连接的KEY
                    DynamicDataSource.dataSourcesMap.put(authorization, druidDataSource);
                    DynamicDataSource.setDataSource(authorization);
                } else {
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
                    DynamicDataSource.dataSourcesMap.put(authorization, druidDataSource);
                    DynamicDataSource.setDataSource(authorization);
                }

            }
        }


//        //这里判断corpId是否为空，如果为空则回到从缓存获取
//        if (!"".equalsIgnoreCase(corpId)) {
//            SwitchDataSource targetDataSource = (SwitchDataSource) targetClass.getAnnotation(SwitchDataSource.class);
//            SwitchDataSource methodDataSource = method.getAnnotation(SwitchDataSource.class);
//            if (targetDataSource != null || methodDataSource != null) {
//                //从Redis上获取律所信息
//                EpmCorpEntity epmCorpEntity = null;
//
//                if (epmCorpEntity == null) {
//                    try {
////                        epmCorpEntity = closeableHttpClientToMaster.MasterGetCropInfo(corpId);
//                        if (epmCorpEntity != null) {
//                            //没有找到corp相关的缓存信息，重新创建
////                            redisUtils.set(corpId + ":corp", epmCorpEntity);
//                        } else {
//                            logger.error("在没有找到corp相关的缓存信息，重新创建时出错，没有找到对应的corp，无法创建缓存");
//                        }
//                    } catch (Exception e) {
//                        logger.error("在没有找到corp相关的缓存信息，重新创建时出错，没有找到对应的corp，错误信息：" + e.toString());
//                    }
//                }
//
//                //获取律所连接
//                CorpDBConnForm corpDBConnForm = JSONObject.parseObject(epmCorpEntity.getDbconnstring(), CorpDBConnForm.class);
//                //根据key向连接池获取连接，如果为空则创建连接并放进连接池,如果不为空就将连接池获取到的连接设置为当前连接
//                //律所ID作为连接的KEY
//                Object dbkey = DynamicDataSource.dataSourcesMap.get(epmCorpEntity.getCorpid());
//                if (dbkey == null) {
//                    DruidDataSource druidDataSource = new DruidDataSource();
//                    druidDataSource.setUrl(corpDBConnForm.getServer() + corpDBConnForm.getDatabase());
//                    druidDataSource.setUsername(corpDBConnForm.getUsername());
//                    druidDataSource.setPassword(corpDBConnForm.getPassword());
//                    druidDataSource.setConnectionErrorRetryAttempts(errorRetry);//连接错误后重试次数
//                    druidDataSource.setTimeBetweenConnectErrorMillis(errorRetryInterval);//连接错误后重试时间
//                    druidDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);//获取连接失败后再次获取
//                    druidDataSource.setMaxWait(maxWait);//最大等待时间，单位毫秒
//                    druidDataSource.setMaxActive(maxActive);//最大连接数
//                    //将创建的连接放到连接池，律所ID作为连接的KEY
//                    DynamicDataSource.dataSourcesMap.put(epmCorpEntity.getCorpid(), druidDataSource);
//                    //设置当前连接
//                    DynamicDataSource.setDataSource(epmCorpEntity.getCorpid());
//                    //流程引擎连接
//                } else {
//                    DynamicDataSource.setDataSource(epmCorpEntity.getCorpid());
//                    //流程引擎连接
//                }
//            }

        try {
            return point.proceed();
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        } finally {
            DynamicDataSource.clear();
            logger.debug("clean datasource");
        }
    }
}
