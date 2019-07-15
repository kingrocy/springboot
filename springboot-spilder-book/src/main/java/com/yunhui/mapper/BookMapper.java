package com.yunhui.mapper;

import com.yunhui.bean.Book;

public interface BookMapper {

    int deleteByPrimaryKey(Integer bookId);


    int insertSelective(Book record);


    Book selectByPrimaryKey(Integer bookId);


    int updateByPrimaryKeySelective(Book record);

}