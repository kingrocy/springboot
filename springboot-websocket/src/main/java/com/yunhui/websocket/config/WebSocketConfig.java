package com.yunhui.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Title: WebSocketConfig.java <br>
 * Description: <br>
 * Copyright (c) 聚阿网络科技版权所有 2018 <br>
 * Create DateTime: 2018年09月29日 15:51 <br>
 *
 * @author yun
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

