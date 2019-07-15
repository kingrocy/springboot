package com.yunhui.springboot.service;

import com.yunhui.springboot.bean.Item;
import com.yunhui.springboot.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-31 13:37
 */
@CacheConfig(cacheNames="redisCacheManager")
@Service
public class ItemService {

    @Autowired
    ItemMapper itemMapper;

    @Cacheable(key = "'item-'+#itemId")
    public Item get(int itemId){
        System.out.println("从数据库中获取");
        return itemMapper.selectByPK(itemId);
    }

    @CachePut(key = "'item-'+#item.itemId")
    public Item add(Item item){
         itemMapper.add(item);
         return item;
    }

    @CachePut(key = "'item-'+#item.itemId")
    public Item update(Item item){
         itemMapper.update(item);
         return item;
    }

    @CacheEvict(key = "'item-'+#itemId")
    public long delete(int itemId){
        return itemMapper.delete(itemId);
    }


}
