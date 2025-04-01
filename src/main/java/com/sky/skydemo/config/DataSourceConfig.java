package com.sky.skydemo.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 配置数据源的类
 */
@Configuration
public class DataSourceConfig {

    /**
     * 创建并返回一个动态数据源，该数据源根据具体需求切换到不同的数据源
     *
     * @return DynamicDataSource 动态数据源实例
     */
    @Bean
    @Primary
    @DependsOn({"springUtils"})
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(DynamicDataSource.dataSourcesMap);
        return dynamicDataSource;
    }
//
//    /**
//     * 创建并返回一个默认的数据源
//     *
//     * @return DruidDataSource 数据源实例
//     */
//    @Bean
//    public DataSource defaultDataSource() {
//        // 创建并返回一个Druid数据源实例
//        return DruidDataSourceBuilder.create().build();
//    }

}
