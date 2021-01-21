package com.yunhui.db.config;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

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
    @Bean(name = "db0")
    @ConfigurationProperties(prefix = "db0")
    public DataSource db0() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 配置数据源1
     *
     * @return
     */
    @Bean(name = "db1")
    @ConfigurationProperties(prefix = "db1")
    public DataSource db1() {
        return DataSourceBuilder.create().build();
    }


    public Map<String, DataSource> dataSourceMap() {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("db0", db0());
        dataSourceMap.put("db1", db1());
        return dataSourceMap;
    }

    /**
     * 配置表规则
     *
     * @return
     */
    @Bean("tableRuleConfig")
    public TableRuleConfiguration tableRuleConfig() {
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration();
        tableRuleConfig.setLogicTable("user");
        tableRuleConfig.setActualDataNodes("db${0..1}.user_${0..1}");

        // 配置分库策略
        tableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "db${user_id % 2}"));

        // 配置分表策略
        tableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "user_${user_id % 2}"));

        return tableRuleConfig;
    }

    /**
     * 配置分片规则
     *
     * @return
     */
    @Bean("shardingRuleConfig")
    public ShardingRuleConfiguration shardingRuleConfig(@Qualifier("tableRuleConfig") TableRuleConfiguration tableRuleConfig) {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfig);
        return shardingRuleConfig;
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource shardingDataSource(@Qualifier("shardingRuleConfig") ShardingRuleConfiguration shardingRuleConfig) throws SQLException {
        // 获取数据源对象
        return ShardingDataSourceFactory.createDataSource(dataSourceMap(), shardingRuleConfig, new ConcurrentHashMap(), new Properties());
    }

}
