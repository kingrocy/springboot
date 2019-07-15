package com.yunhui.controller;

import com.yunhui.bean.Account;
import com.yunhui.bean.User;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-06-04 17:17
 */
@RestController
@RequestMapping("/")
public class CoreController {


    @GetMapping("user/{id}")
    public User get(@PathVariable("id") Long id){
        User user=new User();
        user.setAge(24);
        user.setId(id);
        user.setName("dusy");
        return user;
    }


    @PostMapping("user/login")
    public User login(@RequestBody Account account){
        if(account.getAccount().equals("dusy")&&account.getPasswd().equals("123456")){
            User user=new User();
            user.setAge(24);
            user.setId(123456L);
            user.setName("dusy");
            return user;
        }
        return null;
    }


}
