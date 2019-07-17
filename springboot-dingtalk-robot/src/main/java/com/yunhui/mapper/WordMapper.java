package com.yunhui.mapper;

import com.yunhui.bean.Word;

import java.util.List;

/**
 * @Date : 2019-07-16 17:26
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
public interface WordMapper {

    int addWord(Word word);


    Word randomWord();
}
