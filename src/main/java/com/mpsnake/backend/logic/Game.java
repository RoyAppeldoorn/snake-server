package com.mpsnake.backend.logic;

import com.google.gson.Gson;
import com.mpsnake.backend.dispatcher.MessageDispatcher;
import com.mpsnake.backend.models.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Game implements IGameLogic{

    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    private MessageDispatcher messageDispatcher;

    private static Timer gameTimer;
    private final static long TICK_DELAY = 2000;

    private final ConcurrentHashMap<String, Snake> snakes;
    private final Log log = LogFactory.getLog(Game.class);
    private final Gson gson = new Gson();

    @Autowired
    public Game(MessageDispatcher dispatcher) {
        this.messageDispatcher = dispatcher;
        this.gameTimer = new Timer(Game.class.getSimpleName() + " Timer");
        this.snakes = new ConcurrentHashMap<>();
    }

    public void addPlayerToGame(Snake snake) {
        snakes.put(snake.getSessionId(), snake);

        Message message = new Message(MessageType.JOIN, gson.toJson(snakes.values()));
        messageDispatcher.dispatch(message);

        if(snakes.size() == 1) {
            startGame();
        }
    }

    public void removePlayerFromGame(String sessionId) {
        snakes.remove(sessionId);
        Message message = new Message(MessageType.LEAVE, gson.toJson(sessionId));
        messageDispatcher.dispatch(message);
        resetTimer();
    }

    public void setDirection(String id, Direction direction) {
        Snake snake = snakes.get(id);
        snake.setDirection(direction);
    }

    public void startGame() {
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateSnakes(snakes);
                } catch (Throwable e) {
                    log.error("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    private void resetTimer() {
        gameTimer.cancel();
        gameTimer = new Timer();
    }

    private void updateSnakes(ConcurrentHashMap<String, Snake> snakes) {
        for(Snake snake: snakes.values()) {
            snake.updatePosition();
        }
        Message message = new Message(MessageType.UPDATE, gson.toJson(snakes.values()));
        messageDispatcher.dispatch(message);
    }
}
