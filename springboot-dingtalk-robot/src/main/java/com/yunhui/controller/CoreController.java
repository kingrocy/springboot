package com.yunhui.controller;

import com.alibaba.fastjson.JSON;
import com.yunhui.bean.DingTalkOutgoingResponseBean;
import com.yunhui.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Date : 2019-07-15 13:44
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
@Slf4j
@RestController
public class CoreController {

    final static String TOKEN = "xiaohui1234";


    @PostMapping("/")
    public Map<String,Object> index(HttpServletRequest request) throws Exception {
        //加密方法未知 暂且不校验
        String token = request.getHeader("token");
        ServletInputStream inputStream = request.getInputStream();
        String requestBody = IOUtils.toString(inputStream);
        DingTalkOutgoingResponseBean bean = JSON.parseObject(requestBody, DingTalkOutgoingResponseBean.class);
        return RobotService.process(bean);

    }
}
