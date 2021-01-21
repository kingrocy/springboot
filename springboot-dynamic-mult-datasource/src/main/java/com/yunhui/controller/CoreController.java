package com.yunhui.controller;

import com.yunhui.bean.Db;
import com.yunhui.config.DynamicRoutingDataSource;
import com.yunhui.service.CoreService;
import com.yunhui.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Map;

@RestController
public class CoreController {


    @Autowired
    DynamicRoutingDataSource dynamicDataSource;

    @Autowired
    Map<Object, Object> dbKeyMap;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    CoreService coreService;


    @PostMapping("/addDb")
    public void addDb(Db db){
        dataSourceService.addDataSource(db);
        System.out.println("hello world");
    }


    @GetMapping("/query")
    public Map<String,Object> query(String key,String sql){
        return coreService.query(sql, key);
    }
}
