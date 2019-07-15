package com.yunhui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @Date : 2019-07-15 16:31
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
@Getter
@AllArgsConstructor
public enum MsgTypeEnum {

    TEXT("text"),

    LINK("link"),

    MARKDOWN("markdown"),

    ACTIONCARD("actionCard"),

    FEEDCARD("feedCard")


    ;

    private String type;

}
