package com.yunhui.bean.request;

import com.yunhui.bean.DingTalkOutgoingResponseBean;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Date : 2019-07-15 17:01
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
public abstract class AbstractTextMsgRequestParam extends AbstractMsgRequestParam{


    public abstract String text();

    @Override
    public Map<String, Object> build() {
        super.build();
        if(!StringUtils.isEmpty(text())){
            DingTalkOutgoingResponseBean.TextBean textBean=new DingTalkOutgoingResponseBean.TextBean();
            textBean.setContent(text());
            map.put("text",textBean);
        }
        return map;
    }
}
