package com.mpsnake.backend.websocket.controller;

import com.mpsnake.backend.interfaces.IGameLogic;
import com.mpsnake.backend.model.Direction;
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
    private IGameLogic gameLogic;

    @MessageMapping("/update")
    @SendTo(value = "/topic/public")
    public String broadcast(String msg) throws Exception {
        return msg;
    }

    @MessageMapping("/addUser")
    @SendTo(value = "/topic/public")
    public void addUser(String username) throws Exception {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        Snake newSnake = new Snake(sessionId, username);
        gameLogic.addSnake(newSnake);
    }

    @MessageMapping("/setDirection")
    public void handleDirectionChange(String dir) throws Exception {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        log.info("Player with session id: " + sessionId + " changed its direction to: " + dir);
        try {
            Direction direction = Direction.valueOf(Direction.class, dir.trim());
            gameLogic.setDirection(sessionId, direction);
        } catch(Exception ex) {
            ex.printStackTrace();
            log.warn(ex);
        }
    }
}
