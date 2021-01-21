package com.yunhui.springboot.mapper;

import com.yunhui.springboot.bean.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-16 17:45
 */
public interface ItemMapper {

    Item selectByPK(@Param("itemId") Integer itemId);

    List<Item> listItems();

    long add(Item item);

    long update(@Param("item") Item item);

    long delete(@Param("itemId") int itemId);
}
