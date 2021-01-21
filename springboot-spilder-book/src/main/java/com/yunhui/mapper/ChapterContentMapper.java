package com.yunhui.mapper;

import com.yunhui.bean.ChapterContent;

public interface ChapterContentMapper {

    int deleteByPrimaryKey(Long chapterContentId);

    int insertSelective(ChapterContent record);

    ChapterContent selectByPrimaryKey(Long chapterContentId);

    int updateByPrimaryKeySelective(ChapterContent record);

    int updateByPrimaryKeyWithBLOBs(ChapterContent record);

}