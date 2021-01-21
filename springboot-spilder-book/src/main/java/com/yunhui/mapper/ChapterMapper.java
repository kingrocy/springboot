package com.yunhui.mapper;

import com.yunhui.bean.Chapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChapterMapper {

    int deleteByPrimaryKey(Long chapterId);

    int insertSelective(Chapter record);

    Chapter selectByPrimaryKey(Long chapterId);

    int updateByPrimaryKeySelective(Chapter record);

    List<Chapter> listChapters(@Param("bookId") Long bookId);

    List<Chapter> getChapters(@Param("bookId") Long bookId);

    Chapter getChapterDetail(@Param("chapterId") Long chapterId);
}