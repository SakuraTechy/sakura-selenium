package com.sakura.util;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class ObsRemoteUtil {

    private static final String OBS_WEBSOCKET_URL = "ws://172.19.5.231:4455";
    private static final String OBS_WEBSOCKET_PASSWORD = "your_password";
    private static final String SCENE_NAME = "11";

    public static void main(String[] args) throws IOException, InterruptedException, DeploymentException {
        // 创建WebSocket容器
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        // 创建连接
        Session session = container.connectToServer(new ObsWebSocketEndpoint(), URI.create(OBS_WEBSOCKET_URL));

        // 认证
        authenticate(session, OBS_WEBSOCKET_PASSWORD);

        // 切换场景
        switchScene(session, SCENE_NAME);

        // 开始录制
        startRecording(session);

        // 等待60秒
        Thread.sleep(60 * 1000);

        // 停止录制
        stopRecording(session);

        // 关闭连接
        session.close();
    }

    private static void authenticate(Session session, String password) throws IOException {
        session.getBasicRemote().sendText("{\"request-type\": \"GetAuthRequired\"}");
        session.getBasicRemote().sendText("{\"request-type\": \"Authenticate\", \"auth-token\": \"" + password + "\"}");
    }

    private static void switchScene(Session session, String sceneName) throws IOException {
        session.getBasicRemote().sendText("{\"request-type\": \"SetCurrentProgramScene\", \"scene-name\": \"" + sceneName + "\"}");
    }

    private static void startRecording(Session session) throws IOException {
        session.getBasicRemote().sendText("{\"request-type\": \"StartRecording\"}");
    }

    private static void stopRecording(Session session) throws IOException {
        session.getBasicRemote().sendText("{\"request-type\": \"StopRecording\"}");
    }

    // WebSocket端点
    private static class ObsWebSocketEndpoint {

        private Session session;
        private CountDownLatch latch = new CountDownLatch(1);

        @OnOpen
        public void onOpen(Session session, EndpointConfig config) {
            this.session = session;
            System.out.println("WebSocket connection opened.");
        }

        @OnClose
        public void onClose(Session session) {
            System.out.println("WebSocket connection closed.");
        }

        @OnMessage
        public void onMessage(String message) {
            System.out.println("Received message: " + message);
            latch.countDown(); // 解锁，用于同步
        }
    }
}