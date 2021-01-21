package com.yunhui.bean;

import lombok.Data;

import java.util.List;

@Data
public class DingTalkOutgoingResponseBean {


    private String conversationId;
    private String chatbotUserId;
    private String msgId;
    private String senderNick;
    private boolean isAdmin;
    private long sessionWebhookExpiredTime;
    private long createAt;
    private String conversationType;
    private String senderId;
    private String conversationTitle;
    private boolean isInAtList;
    private String sessionWebhook;
    private TextBean text;
    private String msgtype;
    private List<AtUsersBean> atUsers;


    @Data
    public static class TextBean {
        private String content;
    }

    @Data
    public static class AtUsersBean {
        private String dingtalkId;


    }
}