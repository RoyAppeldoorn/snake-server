package com.mpsnake.backend.websocket.controller;

import com.mpsnake.backend.model.*;
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

    private ArrayList<String> socketConnections;

    private static Timer gameTimer = null;

    private static final long TICK_DELAY = 100;

    private static final ConcurrentHashMap<Integer, Snake> snakes =
            new ConcurrentHashMap<Integer, Snake>();

    private static final AtomicInteger snakeIds = new AtomicInteger(0);

    private final int id;
    private Snake snake;

    public WebSocketController() {
        this.id = snakeIds.getAndIncrement();
    }

    @MessageMapping("/update")
    @SendTo(value = "/topic/location")
    private Location send(Location location) throws Exception {

        return new Location(location.getX()+1, location.getY()+1);
    }

//    @MessageMapping("/move")
//    @SendTo("/topic/location")
//    public Location setDirection(Direction direction) throws Exception {
//        return new Location(100, 20);
//    }

    public static synchronized void addSnake(Snake snake) {
        if (snakes.size() == 0) {
            startTimer();
        }
        snakes.put(Integer.valueOf(snake.getId()), snake);
    }

    public static Collection<Snake> getSnakes() {
        return Collections.unmodifiableCollection(snakes.values());
    }

    public static void startTimer() {
        gameTimer = new Timer(WebSocketController.class.getSimpleName() + " Timer");
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    tick();
                } catch (Throwable e) {
                    log.error("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        this.snake = new Snake(id, event);
        addSnake(snake);
    }
}
