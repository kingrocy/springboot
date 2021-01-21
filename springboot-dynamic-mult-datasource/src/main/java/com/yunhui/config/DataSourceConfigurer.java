package com.yunhui.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataSourceConfigurer {


    @Bean
    public Map<Object, Object> dbKeyMap() {
        Map<Object, Object> map = new ConcurrentHashMap<>();
        map.put("default", defaultDb());
        return map;
    }

    @Bean(name = "default")
    @Primary
    @ConfigurationProperties(prefix = "db")
    public DataSource defaultDb() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Dynamic data source.
     *
     * @return the data source
     */
    @Bean(name = "dynamicDataSource")
    public DynamicRoutingDataSource dynamicDataSource() {

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

        dynamicRoutingDataSource.setDefaultTargetDataSource(defaultDb());

        dynamicRoutingDataSource.setTargetDataSources(dbKeyMap());

        return dynamicRoutingDataSource;
    }


    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryBean") SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate sqlSessionTemplate=new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }


    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dynamicDataSource());
    }

    /**
     * 配置事务管理，如果使用到事务需要注入该 Bean，否则事务不会生效
     * 在需要的地方加上 @Transactional 注解即可
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

}