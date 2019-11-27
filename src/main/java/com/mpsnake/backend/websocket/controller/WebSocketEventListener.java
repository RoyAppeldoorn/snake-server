package com.mpsnake.backend.websocket.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.logging.Logger;

public class WebSocketEventListener {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        headerAccessor.getSessionAttributes();
        logger.info("User Disconnected");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.LEAVE);
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}
