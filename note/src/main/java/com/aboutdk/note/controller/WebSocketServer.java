package com.aboutdk.note.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Component
@ServerEndpoint(value = "/ws/asset")
@Slf4j
public class WebSocketServer {

  private static Map<String, Session> sessionMap = new HashMap<>();

  @OnOpen
  public void onOpen(Session session) {
    String[] split = session.getQueryString().split("=");
    String sessionKey = split[1];
    sessionMap.put(sessionKey, session);

    log.info("有连接加入，当前连接数为：{},sessionId={}", sessionMap.size(), session.getId());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("来自客户端的消息：{}", message);
  }

  @OnClose
  public void onClose(Session session) {
    Set<Map.Entry<String, Session>> entries = sessionMap.entrySet();

    String removeKey = null;
    for (Map.Entry<String, Session> entry : entries) {
      if (entry.getValue().getId().equals(session.getId())) {
        removeKey = entry.getKey();
      }
    }

    if (removeKey != null) {
      sessionMap.remove(removeKey);
      log.info("有连接关闭，当前连接数为：{}", sessionMap.size());
    }
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
    error.printStackTrace();
  }

  public static void sendMessage(Session session, String message) {
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      log.error("发送消息出错：{}", e.getMessage());
      e.printStackTrace();
    }
  }

  public static void broadCastBySessionKey(String curSessionKey, String message) {
    // a demo curSessionKey -> "zhouyang_98498416184984"
    String[] split = curSessionKey.split("_");
    String username = split[0];

    // a list contain the user's sessions(exclude current session, because a user may have several session online).
    List<Session> sessionList = new ArrayList<>();
    Set<Map.Entry<String, Session>> entries = sessionMap.entrySet();
    for (Map.Entry<String, Session> entry : entries) {
      if (entry.getKey().contains(username) && !entry.getKey().equals(curSessionKey)) {
        sessionList.add(entry.getValue());
      }
    }

    for (Session session : sessionList) {
      if (session.isOpen()) {
        sendMessage(session, "update");
      }
    }
  }
}
