package com.mpsnake.backend.websocket.controller;

import com.mpsnake.backend.logic.GameLogic;
import com.mpsnake.backend.model.Snake;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private static final Log log = LogFactory.getLog(WebSocketController.class);

    @Autowired
    private GameLogic gameLogic;

    @MessageMapping("/game.update")
    @SendTo(value = "/topic/public")
    public String newPositions(String msg) throws Exception {
        return msg;
    }

    @MessageMapping("/game.addUser")
    @SendTo(value = "/topic/public")
    public void addUser(String username) throws Exception {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        Snake newSnake = new Snake(sessionId, username);
        gameLogic.addSnake(newSnake);
    }
}
