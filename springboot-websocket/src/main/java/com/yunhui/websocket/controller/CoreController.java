package com.yunhui.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Title: CoreController.java <br>
 * Description: <br>
 * Copyright (c) 聚阿网络科技版权所有 2018 <br>
 * Create DateTime: 2018年09月29日 16:06 <br>
 *
 * @author yun
 */
@RestController
public class CoreController {

    @Autowired
    WebSocketServer socketServer;

    @GetMapping("send")
    public void send(String msg) throws IOException {
//        WebSocketServer.sendInfo(msg);
    }

}
