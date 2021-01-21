package com.yunhui.controller;

import com.yunhui.bean.User;
import com.yunhui.db.config.TargetDataSource;
import com.yunhui.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-13 19:58
 */
@RestController
public class CoreController {


    @Autowired
    UserMapper userMapper;

    @TargetDataSource("db1")
    @GetMapping("/user1")
    public User getUser1() {
        return userMapper.selectByPK(1);
    }

    @TargetDataSource("db2")
    @GetMapping("/user2")
    public User getUser2() {
        return userMapper.selectByPK(1);
    }


}
