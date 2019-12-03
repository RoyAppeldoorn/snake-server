package com.mpsnake.backend.websocket.controller;

import com.mpsnake.backend.model.Snake;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GameLogic {

    private static final Log log = LogFactory.getLog(GameLogic.class);

    public static final AtomicInteger snakeIds = new AtomicInteger(1);

    private static Timer gameTimer = null;

    private static final ConcurrentHashMap<Integer, Snake> snakes =
            new ConcurrentHashMap<Integer, Snake>();

    private static final long TICK_DELAY = 16;

    @Autowired
    private static SimpMessageSendingOperations messageTemplate;

    private static void startTimer() {
        gameTimer = new Timer(GameLogic.class.getSimpleName() + " Timer");
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

                    broadcast(String.format("{\"type\": \"update\", \"data\" : [%s]}",
                            sb.toString()));
                } catch (Throwable e) {
                    log.info("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    private static void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    public static synchronized void addSnake(Snake snake) {
        log.info(snakes.size());
        if (snakes.size() == 0) {
            startTimer();
        }
        snakes.put(Integer.valueOf(snake.getId()), snake);
        log.info(snakes);
    }

    public static synchronized void removeSnake(Snake snake) {
        snakes.remove(Integer.valueOf(snake.getId()));
        if(snakes.size() == 0) {
            stopTimer();
        }
    }

    public static Collection<Snake> getSnakes() {
        return Collections.unmodifiableCollection(snakes.values());
    }

    public static void broadcast(String message) throws Exception{
        for(Snake sn: getSnakes()) {
            log.info(message);
            messageTemplate.convertAndSend("/topic/location", message);
        }
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        int id = GameLogic.snakeIds.getAndIncrement();
        Snake snake = new Snake(id, event);
        GameLogic.addSnake(snake);
    }
}
