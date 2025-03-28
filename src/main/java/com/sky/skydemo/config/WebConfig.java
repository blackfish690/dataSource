package com.sky.skydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DatabaseConnectionInterceptor databaseConnectionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(databaseConnectionInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/ignored/**"); // 排除不需要拦截的路径
    }
}