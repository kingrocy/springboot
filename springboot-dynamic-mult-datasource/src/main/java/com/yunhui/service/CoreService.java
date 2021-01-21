package com.yunhui.service;

import com.yunhui.config.DynamicDataSourceContextHolder;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CoreService {


    @Autowired
    SqlSessionTemplate sqlSessionTemplate;


    @Autowired
    JdbcTemplate jdbcTemplate;

    public Map<String, Object> query(String sql, String dbKey) {
        DynamicDataSourceContextHolder.setDataSourceKey(dbKey);
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        return map;
    }


}
