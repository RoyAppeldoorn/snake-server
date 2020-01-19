package com.mpsnake.backend.socket;

import com.mpsnake.backend.logic.IGameLogic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebsocketGameHandler {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private IGameLogic gameLogic;

    private final Log log = LogFactory.getLog(WebsocketGameHandler.class);

    @EventListener
    private void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Successful connection");
    }

    @EventListener
    private void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        gameLogic.removePlayerFromGame(sessionId);
    }

}
