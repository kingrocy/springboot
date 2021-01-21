package com.yunhui.controller;

import com.alibaba.fastjson.JSON;
import com.yunhui.bean.DingTalkOutgoingResponseBean;
import com.yunhui.constant.YouDictConstant;
import com.yunhui.enums.WordTypeEnum;
import com.yunhui.robot.WordRobot;
import com.yunhui.service.RobotService;
import com.yunhui.service.WordService;
import com.yunhui.spilder.Spilders;
import com.yunhui.utils.HtmlParseUtils;
import com.yunhui.utils.Requests;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class CoreController {

    final static String TOKEN = "xiaohui1234";

    @Autowired
    WordService wordService;

    @Autowired
    WordRobot robot;

    @Autowired
    Spilders spilders;

    @PostMapping("/")
    public Map<String, Object> index(HttpServletRequest request) throws Exception {
        //加密方法未知 暂且不校验
        String token = request.getHeader("token");
        ServletInputStream inputStream = request.getInputStream();
        String requestBody = IOUtils.toString(inputStream);
        DingTalkOutgoingResponseBean bean = JSON.parseObject(requestBody, DingTalkOutgoingResponseBean.class);
        return new RobotService(robot).process(bean);
    }


    @GetMapping("/spilder")
    public void spilder(Integer level) {
        // 页数从0开始
        int[] frequencys = new int[]{1, 2, 3, 4, 5};
        for (int frequency : frequencys) {
            String baseUrl = YouDictConstant.getUrl(WordTypeEnum.getInstance(level), frequency, null);
            String initUrl = YouDictConstant.getUrl(WordTypeEnum.getInstance(level), frequency, 0);
            String html = Requests.html(initUrl);
            spilders.putTaskToQueue(baseUrl, frequency, HtmlParseUtils.getLastPage(html));
        }
    }
}
