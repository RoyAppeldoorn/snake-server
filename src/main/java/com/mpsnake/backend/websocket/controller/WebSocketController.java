package com.mpsnake.backend.websocket.controller;

import com.mpsnake.backend.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class WebSocketController {

    private static final Log log = LogFactory.getLog(WebSocketController.class);

    private static final AtomicInteger snakeIds = new AtomicInteger(1);

    private static Timer gameTimer = null;

    private static final ConcurrentHashMap<Integer, Snake> snakes =
            new ConcurrentHashMap<Integer, Snake>();

    private static final long TICK_DELAY = 100;

    @MessageMapping("/update")
    @SendTo(value = "/topic/location")
    public static String newPositions(String msg) throws Exception {
        log.info(msg);
        return msg;
    }

    public static void startTimer() {
        gameTimer = new Timer(WebSocketController.class.getSimpleName() + " Timer");
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    StringBuilder sb = new StringBuilder();
                    for(Iterator<Snake> iterator = getSnakes().iterator();
                        iterator.hasNext();) {
                        Snake snake = iterator.next();
                        sb.append(snake.getLocationsJson());
                        if (iterator.hasNext()) {
                            sb.append(',');
                        }
                    }

                    broadcast(String.format("{'type': 'update', 'data' : [%s]}",
                            sb.toString()));
                } catch (Throwable e) {
                    log.info("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    public static void broadcast(String message) throws Exception{
        for(Snake sn: getSnakes()) {
            WebSocketController.newPositions(message);
        }
    }


    public static synchronized void addSnake(Snake snake) {
        if (snakes.size() == 0) {
            startTimer();
        }
        snakes.put(Integer.valueOf(snake.getId()), snake);
        log.info(snakes);
    }

    public static Collection<Snake> getSnakes() {
        return Collections.unmodifiableCollection(snakes.values());
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        int id = snakeIds.getAndIncrement();
        Snake snake = new Snake(id, event);
        log.info(snake.getId());
        addSnake(snake);
    }
}
