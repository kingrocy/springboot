package com.yunhui.springboot.test;
import com.yunhui.springboot.Application;
import com.yunhui.springboot.bean.Item;
import com.yunhui.springboot.mapper.ItemMapper;
import com.yunhui.springboot.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@EnableAutoConfiguration
public class TestRedisSort {



    @Autowired
    RedisService redisService;


    @Autowired
    ItemMapper itemMapper;

    @Test
    public void test(){

        List<Item> items=itemMapper.listItems();

        for(Item item:items){
            redisService.setObjectToJSON("item-"+item.getItemId(),item);
        }

        List<String> list = items.stream().map(Item::getItemId).map((l) -> String.valueOf(l)).collect(Collectors.toList());

        redisService.lpush("item-list-key", list.toArray(new String[list.size()]));
    }

    @Test
    public void test1(){

        List<String> strs = redisService.lrange("item-list-key", 0, -1);
        for(String str:strs){
            System.out.println(str);
        }

    }

    @Test
    public void test3(){
        List<Item> items=itemMapper.listItems();
        for(Item item:items){
            redisService.hset("item-sort-"+item.getItemId(),"price",item.getItemPrice().toString());
            redisService.hset("item-sort-"+item.getItemId(),"sales",item.getItemSales().toString());
        }

    }

    @Test
    public void test2(){
        Jedis jedis = redisService.getPool().getResource();

        String key="item-list-key";

        String sortKey="item-sort";

        SortingParams sortingParams=new SortingParams();

        sortingParams.by(sortKey+"-*->price");

        sortingParams.desc();

        sortingParams.get("#");

        sortingParams.get(sortKey+"-*->price");

        sortingParams.get(sortKey+"-*->sales");


        List<String> sort = jedis.sort(key, sortingParams);

        for(String str:sort){
            System.out.println(str);
        }

    }

}
