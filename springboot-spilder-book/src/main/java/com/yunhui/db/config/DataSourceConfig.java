package com.yunhui.db.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @Author: Yun
 * @Description: 数据源及分表策略
 * @Date: Created in 2018-03-30 17:10
 */
@Configuration
public class DataSourceConfig {

    /**
     * 配置数据源1
     *
     * @return
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "db")
    public DataSource db() {
        return DataSourceBuilder.create().build();
    }


}
