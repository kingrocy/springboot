package com.yunhui.service;
import com.yunhui.bean.DingTalkOutgoingResponseBean;
import com.yunhui.bean.request.AtParam;
import com.yunhui.bean.request.CommonMsgRequestParam;

import java.util.Map;

/**
 * @Date : 2019-07-15 16:23
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
public class RobotService {

    public static Map<String,Object> process(DingTalkOutgoingResponseBean bean){
        switch (bean.getText().getContent().trim()){
            case ":help":
                break;
            case ":word":
                break;
            default:
                    break;
        }
        return CommonMsgRequestParam.text(bean.getText().getContent(), AtParam.build().dingtalkIds(bean.getSenderId()));
    }
}
