package com.yunhui.bean.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AtParam {

    private List<String> atMobiles = new ArrayList<>();
    private List<String> atDingtalkIds = new ArrayList<>();
    private boolean isAtAll;


    public static AtParam build() {
        return new AtParam();
    }

    public AtParam mobile(String mobile) {
        this.atMobiles.add(mobile);
        return this;
    }

    public AtParam mobiles(String... mobiles) {
        for (String mobile : mobiles) {
            this.atMobiles.add(mobile);
        }
        return this;
    }


    public AtParam dingtalkId(String dingtalkId) {
        this.atDingtalkIds.add(dingtalkId);
        return this;
    }

    public AtParam dingtalkIds(String... dingtalkIds) {
        for (String dingtalkId : dingtalkIds) {
            this.atDingtalkIds.add(dingtalkId);
        }
        return this;
    }

    public AtParam isAtAll(Boolean isAtAll) {
        this.isAtAll = isAtAll;
        return this;
    }

}
