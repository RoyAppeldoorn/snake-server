package com.mpsnake.backend.logic;

import com.mpsnake.backend.model.Snake;
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

    private final long TICK_DELAY = 5000;

    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    private Collection<Snake> getSnakes() {
        return Collections.unmodifiableCollection(snakes.values());
    }

    public synchronized void addSnake(String id) {
        if (snakes.size() == 0) {
            startTimer();
        }

        Snake snake = new Snake(id, " ");
        snakes.put(String.valueOf(snake.getId()), snake);
        try {
            addSnakeBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeSnake(String id) {
        snakes.remove(id);
        if(snakes.size() == 0) {
            stopTimer();
        }
        try {
            removeSnakeBroadcast(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            sb.append(snake.getLocationJson());
            if (iterator.hasNext()) {
                sb.append(',');
            }
        }

        broadcast(String.format("{\"type\": \"update\", \"data\" : [%s]}",
                sb.toString()));
    }

    private void addSnakeBroadcast() throws Exception {
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

    private void removeSnakeBroadcast(String id) throws Exception {
        broadcast(String.format("{\"type\": \"leave\", \"id\" : \"%s\"}",
                String.valueOf(id)));
    }

    private void broadcast(String message) throws Exception {
        log.info(message);
        messageTemplate.convertAndSend("/topic/location", message);
    }
}
