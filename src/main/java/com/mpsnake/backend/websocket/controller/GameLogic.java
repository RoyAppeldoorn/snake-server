package com.mpsnake.backend.websocket.controller;

import com.mpsnake.backend.model.Snake;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GameLogic {

    private final Log log = LogFactory.getLog(GameLogic.class);

    private final AtomicInteger snakeIds = new AtomicInteger(1);

    private Timer gameTimer = null;

    private final ConcurrentHashMap<Integer, Snake> snakes =
            new ConcurrentHashMap<Integer, Snake>();

    private final long TICK_DELAY = 100;

    private final WebSocketController wc = new WebSocketController();

    public void startTimer() {
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

                    broadcast(String.format("{'type': 'update', 'data' : [%s]}",
                            sb.toString()));
                } catch (Throwable e) {
                    log.info("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    public synchronized void addSnake(Snake snake) {
        if (snakes.size() == 0) {
            startTimer();
        }
        snakes.put(Integer.valueOf(snake.getId()), snake);
        log.info(snakes);
    }

    public Collection<Snake> getSnakes() {
        return Collections.unmodifiableCollection(snakes.values());
    }

    public void broadcast(String message) throws Exception{
        for(Snake sn: getSnakes()) {
            wc.newPositions(message);
        }
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        int id = snakeIds.getAndIncrement();
        Snake snake = new Snake(id, event);
        log.info(snake.getId());
        addSnake(snake);
    }
}
