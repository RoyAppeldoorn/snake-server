package com.mpsnake.backend.websocket.controller;

import com.google.gson.Gson;
import com.mpsnake.backend.model.*;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebSocketController {

    private Snake snake;

    @MessageMapping("/update")
    @SendTo("/topic/location")
    public Location send(Location location) throws Exception {
        return new Location(50, 50);
    }

    @MessageMapping("/move")
    @SendTo("/topic/location")
    public Location setDirection(Direction direction) throws Exception {
        return new Location(100, 20);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

    }
}
