package com.yunhui.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.yunhui.websocket.bean.Msg;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title: WebSocketServer.java <br>
 * Description: <br>
 * Copyright (c) 聚阿网络科技版权所有 2018 <br>
 * Create DateTime: 2018年09月29日 15:52 <br>
 *
 * @author yun
 */
@ServerEndpoint(value = "/websocket/{name}")
@Component
public class WebSocketServer {

    private static int onlineCount = 0;

    private static ConcurrentHashMap<String, WebSocketServer> map = new ConcurrentHashMap<>();

    private Session session;

    private String name;

    /**
     * 群发自定义消息
     */
    public static void batchSendInfo(Msg msg) throws IOException {
        Set<Map.Entry<String, WebSocketServer>> entries = map.entrySet();
        for (Map.Entry<String, WebSocketServer> entry : entries) {
            if (!entry.getKey().equals(msg.getName())) {
                entry.getValue().sendMessage(msg);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) throws IOException {
        this.name = name;
        this.session = session;
        map.put(name, this);
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        Msg msg = new Msg("系统", "欢迎[" + name + "]加入");
        batchSendInfo(msg);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("name") String name) throws IOException {
        map.remove(name);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        Msg msg = new Msg("系统", "[" + name + "]退出");
        batchSendInfo(msg);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("name") String name) throws IOException {
        System.out.println("来自客户端[" + name + "]的消息:" + message);
        Msg msg = new Msg(name, message);
        batchSendInfo(msg);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(Msg msg) throws IOException {
        this.session.getBasicRemote().sendText(JSON.toJSONString(msg));
    }
}
