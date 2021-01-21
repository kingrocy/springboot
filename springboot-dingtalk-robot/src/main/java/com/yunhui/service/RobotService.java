package com.yunhui.service;

import com.yunhui.bean.DingTalkOutgoingResponseBean;
import com.yunhui.bean.request.AtParam;
import com.yunhui.bean.request.CommonMsgRequestParam;
import com.yunhui.bean.request.MdParam;
import com.yunhui.constant.TranslateConstant;
import com.yunhui.enums.CommandEnum;
import com.yunhui.robot.WordRobot;
import com.yunhui.utils.BaiDuTranslateApiUtils;
import com.yunhui.utils.MarkDownUtils;

import java.util.Map;

public class RobotService {

    private WordRobot robot;

    public RobotService(WordRobot robot) {
        this.robot = robot;
    }

    public static void main(String[] args) {
        String str = ":hello";
        System.out.println(str.replace(":h", ""));
    }

    public Map<String, Object> process(DingTalkOutgoingResponseBean bean) {
        String content = bean.getText().getContent().trim();
        if (content.startsWith(CommandEnum.WORD.getCommand())) {
            robot.sendWord();
            return null;
        } else if (content.startsWith(CommandEnum.TRANSLATE_EN.getCommand())) {
            String result = BaiDuTranslateApiUtils.doTranslate(content.replace(CommandEnum.TRANSLATE_EN.getCommand(), "")
                    , TranslateConstant.EN, TranslateConstant.ZH);
            return CommonMsgRequestParam.text(result, AtParam.build().dingtalkIds(bean.getSenderId()));
        } else if (content.startsWith(CommandEnum.TRANSLATE_ZH.getCommand())) {
            String result = BaiDuTranslateApiUtils.doTranslate(content.replace(CommandEnum.TRANSLATE_ZH.getCommand(), "")
                    , TranslateConstant.ZH, TranslateConstant.EN);
            return CommonMsgRequestParam.text(result, AtParam.build().dingtalkIds(bean.getSenderId()));
        } else {
            //回复命令列表
            return CommonMsgRequestParam.markdown(new MdParam("机器人指令列表",
                            buildCommandMD())
                    , AtParam.build().dingtalkIds(bean.getSenderId()));
        }
    }

    public String buildCommandMD() {
        MarkDownUtils builder = MarkDownUtils.builder();
        for (CommandEnum value : CommandEnum.values()) {
            builder.h1(value.getCommand());
            builder.ul("描述: *" + value.getDesc() + "*");
            builder.ul("举例：*" + value.getExample() + "*");
        }
        return builder.build();
    }
}
