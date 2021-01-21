package com.yunhui.bean.request;

import com.yunhui.enums.MsgTypeEnum;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMsgRequestParam {

    protected final Map<String, Object> map = new HashMap<>();


    public abstract MsgTypeEnum msgtype();


    public abstract AtParam at();


    public Map<String, Object> build() {
        this.map.put("msgtype", msgtype().getType());
        this.map.put("at", at());
        return map;
    }

}
