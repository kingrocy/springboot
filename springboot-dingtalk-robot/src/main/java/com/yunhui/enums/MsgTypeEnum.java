package com.yunhui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgTypeEnum {

    TEXT("text"),

    LINK("link"),

    MARKDOWN("markdown"),

    ACTIONCARD("actionCard"),

    FEEDCARD("feedCard");

    private String type;

}
