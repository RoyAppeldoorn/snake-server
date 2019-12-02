package com.mpsnake.backend.websocket.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private static final Log log = LogFactory.getLog(WebSocketController.class);

    @MessageMapping("/update")
    @SendTo(value = "/topic/location")
    public String newPositions(String msg) throws Exception {
        log.info(msg);
        return msg;
    }

}
