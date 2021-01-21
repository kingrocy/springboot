package com.yunhui.bean.request;

import com.yunhui.bean.DingTalkOutgoingResponseBean;
import com.yunhui.enums.MsgTypeEnum;
import org.springframework.util.StringUtils;

import java.util.Map;

public abstract class AbstractTextMsgRequestParam extends AbstractMsgRequestParam {


    public abstract String text();

    @Override
    public MsgTypeEnum msgtype() {
        return MsgTypeEnum.TEXT;
    }

    @Override
    public Map<String, Object> build() {
        super.build();
        if (!StringUtils.isEmpty(text())) {
            DingTalkOutgoingResponseBean.TextBean textBean = new DingTalkOutgoingResponseBean.TextBean();
            textBean.setContent(text());
            map.put("text", textBean);
        }
        return map;
    }
}
