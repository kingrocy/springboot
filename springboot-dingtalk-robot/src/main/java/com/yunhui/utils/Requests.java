package com.yunhui.utils;

import com.alibaba.fastjson.JSON;
import com.yunhui.constant.RobotConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Date : 2019-07-16 09:43
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
@Slf4j
public class Requests {


    public static String html(String url){
        String html= null;
        try {
            html = Request.Get(url).execute().returnContent().asString(Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("execute request error", e);
        }
        return html;
    }


    public static void dingtalk(Map<String ,Object> map){
        try {
            Request.Post(RobotConstant.HOOK_URL)
                    .bodyString(JSON.toJSONString(map), ContentType.APPLICATION_JSON)
                    .execute();
        } catch (IOException e) {
            log.error("execute request error", e);
        }
    }
}
