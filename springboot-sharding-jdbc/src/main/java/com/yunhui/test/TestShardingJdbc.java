package com.yunhui.test;

import com.yunhui.Application;
import com.yunhui.bean.User;
import com.yunhui.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@EnableAutoConfiguration
public class TestShardingJdbc {


    @Autowired
    UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println(userMapper.selectByPK(1));
    }


    @Test
    public void test2() {

        User user = new User();

        user.setUserId(3);
        user.setUserName("jerry");
        user.setUserAge(2);

        userMapper.insertSelective(user);

    }
}
