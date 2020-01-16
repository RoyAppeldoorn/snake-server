package com.mpsnake.backend;

import com.mpsnake.backend.interfaces.IGameLogic;
import com.mpsnake.backend.models.Game;
import com.mpsnake.backend.models.Snake;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class GameHandler {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private IGameLogic gameLogic;

    private final Log log = LogFactory.getLog(GameHandler.class);

    @EventListener
    private void handleWebSocketConnectListener(SessionConnectedEvent event) throws Exception {
        log.info("Successful connection");
    }

    @EventListener
    private void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        gameLogic.removeSnake(sessionId);
    }

    public void initGame() {
        Game game = new Game();
        Snake snake = new Snake()
    }


}
