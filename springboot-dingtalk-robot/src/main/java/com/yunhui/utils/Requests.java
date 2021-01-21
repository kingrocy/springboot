package com.yunhui.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunhui.constant.RobotConstant;
import com.yunhui.constant.TranslateConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
public class Requests {


    public static String html(String url) {
        String html = null;
        try {
            html = Request.Get(url).execute().returnContent().asString(Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("execute request error", e);
        }
        return html;
    }


    public static void dingtalk(Map<String, Object> map) {
        try {
            Response response = Request.Post(RobotConstant.HOOK_URL)
                    .bodyString(JSON.toJSONString(map), ContentType.APPLICATION_JSON)
                    .execute();
            log.info("dingtalk result:{}", response.returnContent().asString());
        } catch (IOException e) {
            log.error("execute request error", e);
        }
    }


    public static JSONObject translate(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(TranslateConstant.BAIDU_TRANSLATE_API);
        sb.append("?");
        map.forEach((k, v) -> {
            sb.append(k);
            sb.append("=");
            try {
                sb.append(URLEncoder.encode(String.valueOf(v), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&");
        });
        log.info("translate url:" + sb.toString());
        String content = null;
        try {
            content = Request.Get(sb.toString())
                    .execute().returnContent().asString(Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("execute request error", e);
        }
        return JSON.parseObject(content);
    }

}
