package com.sky.skydemo.dynamicdatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

//    private String defaultUrl = "jdbc:mysql://121.37.11.215:3306/sky-mall?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
//    private String defaultUsername = "root";
//    private String defaultPassword ="U2jbmZXTVK";
//
//    private String secondUrl = "jdbc:mysql://121.37.11.215:3306/sky-mall-test?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
//    private String secondUsername = "root";
//    private String secondPassword="U2jbmZXTVK";
    @Autowired
    private DatabaseConnectionInterceptor databaseConnectionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(databaseConnectionInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/ignored/**"); // 排除不需要拦截的路径
    }
}