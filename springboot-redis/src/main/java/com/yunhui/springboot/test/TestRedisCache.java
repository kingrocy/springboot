package com.yunhui.springboot.test;

import com.yunhui.springboot.Application;
import com.yunhui.springboot.bean.Item;
import com.yunhui.springboot.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-31 14:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@EnableAutoConfiguration
public class TestRedisCache {


    @Autowired
    ItemService itemService;


    @Test
    public void test1() {
        Item item = itemService.get(1);
        System.out.println(item);
    }

    @Test
    public void test2() {
        Item item = new Item();
        item.setItemPrice(18.8);
        item.setItemSales(88L);
        itemService.add(item);
        System.out.println(item);
    }

    @Test
    public void test3() {
        Item item = new Item();
        item.setItemId(22);
        item.setItemPrice(1218.8);
        item.setItemSales(1288L);
        itemService.update(item);
    }

    @Test
    public void test4() {
        itemService.delete(22);
    }

}
