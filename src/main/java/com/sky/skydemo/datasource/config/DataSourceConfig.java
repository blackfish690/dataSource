package com.sky.skydemo.datasource.config;

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
     * 创建并返回一个默认的数据源
     *
     * @return DruidDataSource 数据源实例
     */
    @Bean
    // 可以使用@ConfigurationProperties注解直接绑定配置文件中的属性，此处已注释掉
    public DataSource defaultDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 创建并返回一个动态数据源，该数据源根据具体需求切换到不同的数据源
     *
     * @return DynamicDataSource 动态数据源实例
     */
    @Bean
    @Primary
    @DependsOn({"springUtils", "defaultDataSource"})
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(DynamicDataSource.dataSourcesMap);
        return dynamicDataSource;
    }
}
