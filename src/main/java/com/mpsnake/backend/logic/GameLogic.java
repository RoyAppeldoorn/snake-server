package com.mpsnake.backend.logic;

import com.mpsnake.backend.interfaces.IGameLogic;
import com.mpsnake.backend.models.Direction;
import com.mpsnake.backend.models.Snake;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameLogic implements IGameLogic {

    private final Log log = LogFactory.getLog(GameLogic.class);

    private Timer gameTimer = null;

    private final ConcurrentHashMap<String, Snake> snakes =
            new ConcurrentHashMap<String, Snake>();

    private final long TICK_DELAY = 200;

    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    private Collection<Snake> getSnakes() {
        return Collections.unmodifiableCollection(snakes.values());
    }

    public synchronized void addSnake(Snake snake) {
        if (snakes.size() == 0) {
            startTimer();
        }

        snakes.put(String.valueOf(snake.getId()), snake);
        addSnakeBroadcast();
    }

    public synchronized void removeSnake(String id) {
        snakes.remove(id);
        if(snakes.size() == 0) {
            stopTimer();
        }
        removeSnakeBroadcast(id);
    }

    public synchronized void setDirection(String id, Direction direction) {
        Snake snake = snakes.get(id);
        snake.setDirection(direction);
    }

    private void startTimer() {
        gameTimer = new Timer(GameLogic.class.getSimpleName() + " Timer");
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateSnakeBroadcast();
                } catch (Throwable e) {
                    log.info("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    private void updateSnakeBroadcast() throws Exception {
        StringBuilder sb = new StringBuilder();
        for(Iterator<Snake> iterator = getSnakes().iterator();
            iterator.hasNext();) {
            Snake snake = iterator.next();
            snake.update(getSnakes());
            sb.append(snake.getLocationJson());
            if (iterator.hasNext()) {
                sb.append(',');
            }
        }

        broadcast(String.format("{\"type\": \"update\", \"data\" : [%s]}",
                sb.toString()));
    }

    private void addSnakeBroadcast() {
        StringBuilder sbAddSnake = new StringBuilder();
        for(Iterator<Snake> iterator = getSnakes().iterator();
            iterator.hasNext();) {
            Snake snake = iterator.next();
            sbAddSnake.append(String.format("{\"id\": \"%s\", \"color\": \"%s\"}",
                    String.valueOf(snake.getId()), snake.getHexColor()));
            if (iterator.hasNext()) {
                sbAddSnake.append(',');
            }
        }
        broadcast(String.format("{\"type\": \"join\", \"data\" : [%s]}",
                sbAddSnake.toString()));
    }

    private void removeSnakeBroadcast(String id) {
        broadcast(String.format("{\"type\": \"leave\", \"id\" : \"%s\"}",
                String.valueOf(id)));
    }

    private void broadcast(String message) {
        log.info(message);
        messageTemplate.convertAndSend("/topic/public", message);
    }
}
