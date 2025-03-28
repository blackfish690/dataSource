package com.sky.skydemo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DataSourceConfig {

    @Bean(name = "ds1")
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource ds1() {
        return DataSourceBuilder.create().build();
    }

//    @Bean(name = "ds2")
//    @ConfigurationProperties("spring.shardingsphere.datasource.second-datasource")
//    public DataSource ds2() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean(name = "DataSource")
    public DataSource DynamicDataSource(@Qualifier("ds1") DataSource ds1) {
        HashMap<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put("ds1", ds1);
//        targetDataSource.put("ds2", ds2);
        //实例化DynamicDataSource,设置数据源map
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSource);
        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(ds1);
        return dynamicDataSource;
    }


}
