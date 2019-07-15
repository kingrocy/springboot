package com.yunhui.bean.request;

import com.yunhui.enums.MsgTypeEnum;

import java.util.Map;

/**
 * @Date : 2019-07-15 17:04
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
public class CommonMsgRequestParam {

    public static Map<String, Object> text(String text, AtParam atParam) {
        return new AbstractTextMsgRequestParam() {
            @Override
            public MsgTypeEnum msgtype() {
                return MsgTypeEnum.TEXT;
            }
            @Override
            public AtParam at() {
                return atParam;
            }
            @Override
            public String text() {
                return text;
            }
        }.build();
    }
}