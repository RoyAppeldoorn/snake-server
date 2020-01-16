package com.mpsnake.backend.controller;

import com.mpsnake.backend.interfaces.IGameLogic;
import com.mpsnake.backend.models.Direction;
import com.mpsnake.backend.models.Snake;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final Log log = LogFactory.getLog(GameController.class);

    @Autowired
    private IGameLogic gameLogic;

    @MessageMapping("/update")
    @SendTo(value = "/topic/public")
    public String broadcast(String msg) {
        return msg;
    }

    @MessageMapping("/addUser")
    @SendTo(value = "/topic/public")
    public void addUser(String username) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        Snake newSnake = new Snake(sessionId, username);
        gameLogic.addSnake(newSnake);
    }

    @MessageMapping("/setDirection")
    public void handleDirectionChange(String dir) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        log.info("Player with session id: " + sessionId + " changed its direction to: " + dir);
        try {
            Direction direction = Direction.valueOf(Direction.class, dir);
            gameLogic.setDirection(sessionId, direction);
        } catch(Exception ex) {
            log.error(ex);
        }
    }
}
