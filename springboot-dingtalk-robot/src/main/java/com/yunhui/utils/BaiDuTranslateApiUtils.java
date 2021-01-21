package com.yunhui.utils;

import com.alibaba.fastjson.JSONObject;
import com.yunhui.constant.TranslateConstant;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BaiDuTranslateApiUtils {

    public static String salt() {
        return String.valueOf(System.currentTimeMillis()).substring(4, 8);
    }


    public static String sign(String query, String salt) {
        StringBuilder sb = new StringBuilder();
        sb.append(TranslateConstant.BAIDU_TRANSLATE_APP_KEY);
        sb.append(query);
        sb.append(salt);
        sb.append(TranslateConstant.BAIDU_TRANSLATE_APP_SECRET);
        return DigestUtils.md5Hex(sb.toString());
    }


    public static String doTranslate(String query, String from, String to) {
        try {
            query = new String(query.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("from", from);
        paramMap.put("to", to);
        paramMap.put("appid", TranslateConstant.BAIDU_TRANSLATE_APP_KEY);
        String salt = salt();
        paramMap.put("salt", salt);
        paramMap.put("sign", sign(query, salt));
        JSONObject translate = Requests.translate(paramMap);
        System.out.println(translate);
        if (translate.containsKey("error_code")) {
            return "翻译错误";
        }
        String dst = translate.getJSONArray("trans_result").getJSONObject(0).getString("dst");
        return dst;

    }

    public static void main(String[] args) {
        System.out.println(doTranslate("切割", "zh", "en"));
    }

}
