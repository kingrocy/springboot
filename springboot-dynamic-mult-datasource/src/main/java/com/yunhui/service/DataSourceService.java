package com.yunhui.service;

import com.yunhui.bean.Db;
import com.yunhui.enums.DriverClassEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Service
public class DataSourceService {

    @Autowired
    Map<Object, Object> dbKeyMap;


    public void addDataSource(Db db){
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(DriverClassEnum.getDriverClassByType(db.getType()))
                .password(db.getPasswd())
                .url(db.getUrl())
                .username(db.getUser())
                .build();
        dbKeyMap.put(db.getKey(),dataSource);
    }

}
