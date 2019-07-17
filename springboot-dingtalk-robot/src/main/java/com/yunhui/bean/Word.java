package com.yunhui.bean;

import lombok.Data;

/**
 * @Date : 2019-07-16 17:25
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
@Data
public class Word {

    private Long id;
    private String word;
    private String desc;
    private String pic;
    private Integer type;
}
