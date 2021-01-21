package com.yunhui.springboot.controller;

import com.yunhui.springboot.bean.Item;
import com.yunhui.springboot.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-31 15:24
 */
@RestController
public class CoreController {

    @Autowired
    ItemService itemService;

    @GetMapping("/get/{id}")
    public Item getItem(@PathVariable("id") Integer id) {
        return itemService.get(id);
    }

}
