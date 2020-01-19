package com.mpsnake.backend.logic;

import com.google.gson.Gson;
import com.mpsnake.backend.dispatcher.MessageDispatcher;
import com.mpsnake.backend.models.*;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class Game implements IGameLogic{

    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    @Autowired
    private MessageDispatcher messageDispatcher;

    private static Timer gameTimer;
    private static final long TICK_DELAY = 1000;
    private final ConcurrentHashMap<String, Snake> snakes;
    private int round;

    private final Log log = LogFactory.getLog(Game.class);
    private final Gson gson = new Gson();

    public Game() {
        this.gameTimer = new Timer();
        this.snakes = new ConcurrentHashMap<>();
        this.round = 0;
    }

    public void addPlayerToGame(Snake snake) {
        snakes.put(snake.getSessionId(), snake);

        Message message = new Message(MessageType.JOIN, gson.toJson(snakes.values()));
        messageDispatcher.dispatch(message);

        if(snakes.size() == 2) {
            startGame();
        }
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

    private void updateSnakes(ConcurrentHashMap<String, Snake> snakes) {
        for(Snake snake: snakes.values()) {
            snake.updatePosition();
            handleCollision(snake, snakes.values());
        }
        Message message = new Message(MessageType.UPDATE, gson.toJson(snakes.values()));
        messageDispatcher.dispatch(message);
    }

    private void handleCollision(Snake currentSnake, Collection<Snake> snakes) {
        for(Snake snake: snakes) {
            boolean headCollision = !currentSnake.getSessionId().equals(snake.getSessionId()) && snake.getHead().equals(currentSnake.getHead());
            boolean tailCollision = snake.getTail().contains(currentSnake.getHead());
            if (headCollision || tailCollision) {
                if(!currentSnake.getSessionId().equals(snake.getSessionId())) {
                    increaseSnakePoint(snake);
                } else {
                    dead(currentSnake);
                }
                clearFieldAfterRound();
            }
        }

    }

    private void dead(Snake snake) {
        Message message = new Message(MessageType.DEAD, gson.toJson(snake));
        messageDispatcher.dispatch(message);
        increaseRound();
    }

    private void increaseSnakePoint(Snake snake) {
        snake.setPoints(snake.getPoints() + 1);
        Message message = new Message(MessageType.KILL, gson.toJson(snake));
        messageDispatcher.dispatch(message);
        increaseRound();
    }

    public void removePlayerFromGame(String sessionId) {
        snakes.remove(sessionId);
        Message message = new Message(MessageType.LEAVE, gson.toJson(sessionId));
        messageDispatcher.dispatch(message);

        if(snakes.size() == 0) {
            endGame();
        }
    }

    public void setDirection(String id, Direction direction) {
        Snake snake = snakes.get(id);
        snake.setDirection(direction);
    }

    private void increaseRound() {
        Message roundOverMessage = new Message(MessageType.ROUND_OVER, null);
        messageDispatcher.dispatch(roundOverMessage);

        round++;
        if(round == 3) {
            Message gameOverMessage = new Message(MessageType.GAME_OVER, null);
            messageDispatcher.dispatch(gameOverMessage);
            endGame();
        }
    }

    private void clearFieldAfterRound() {
        for(Snake snake: snakes.values()) {
            snake.resetState();
        }
    }

    private void endGame() {
        for(Snake snake: snakes.values()) {
            removePlayerFromGame(snake.getSessionId());
        }
        resetTimer();
    }

    private void resetTimer() {
        gameTimer.cancel();
        gameTimer = new Timer();
    }
}
