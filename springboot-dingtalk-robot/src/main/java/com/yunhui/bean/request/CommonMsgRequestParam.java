package com.yunhui.bean.request;

import java.util.Map;

public class CommonMsgRequestParam {

    public static Map<String, Object> text(String text, AtParam atParam) {
        return new AbstractTextMsgRequestParam() {
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


    public static Map<String, Object> markdown(MdParam param, AtParam atParam) {
        return new AbstractMdMsgRequestParam() {
            @Override
            public AtParam at() {
                return atParam;
            }

            @Override
            public MdParam markdown() {
                return param;
            }
        }.build();
    }
}
