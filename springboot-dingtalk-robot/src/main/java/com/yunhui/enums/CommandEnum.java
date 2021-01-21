package com.yunhui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandEnum {


    HELP(":h", "帮助", ":h"),
    TRANSLATE_ZH(":tz", "中文翻译", ":tz 你叫什么名字"),
    TRANSLATE_EN(":te", "英文翻译", ":te my name is whh"),
    WORD(":w", "随机发送单词", ":w");

    private String command;
    private String desc;
    private String example;
}
