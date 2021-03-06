package com.yunhui.bean.request;

import com.yunhui.enums.MsgTypeEnum;

import java.util.Map;

public abstract class AbstractMdMsgRequestParam extends AbstractMsgRequestParam {

    @Override
    public MsgTypeEnum msgtype() {
        return MsgTypeEnum.MARKDOWN;
    }

    @Override
    public Map<String, Object> build() {
        super.build();
        map.put("markdown", markdown());
        return map;
    }

    public abstract MdParam markdown();

}
