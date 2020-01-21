package com.mpsnake.backend.logic;

import com.google.gson.Gson;
import com.mpsnake.backend.dispatcher.MessageDispatcher;
import com.mpsnake.backend.models.*;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class Game implements IGameLogic{

    private final SimpMessageSendingOperations messageTemplate;
    private final MessageDispatcher messageDispatcher;

    private static Timer gameTimer = new Timer();
    private static final long TICK_DELAY = 300;
    private final ConcurrentHashMap<String, Snake> snakes;
    private int round;

    private final Log log = LogFactory.getLog(Game.class);
    private final Gson gson = new Gson();

    @Autowired
    public Game(MessageDispatcher messageDispatcher, SimpMessageSendingOperations messageTemplate) {
        this.messageDispatcher = messageDispatcher;
        this.messageTemplate = messageTemplate;
        this.snakes = new ConcurrentHashMap<>();
        this.round = 0;
    }

    public void addPlayerToGame(Snake snake) {
        snakes.put(snake.getSessionId(), snake);
        if(snakes.size() == 1) {
            snake.setRoomMaster(true);
        }
        Message message = new Message(MessageType.JOIN, gson.toJson(snakes.values()));
        messageDispatcher.dispatch(message);
    }

    public void startGame() {
        resetGame();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateSnakes(snakes);
                } catch (Exception e) {
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
        log.info(message.getContent());
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
                    dead(snake);
                    givePointsToOtherSnakes(currentSnake, snakes);
                }
                increaseRound();
            }
        }
    }

    private void givePointsToOtherSnakes(Snake snake, Collection<Snake> snakes) {
        for(Snake s: snakes) {
            if(!s.getSessionId().equals(snake.getSessionId())) {
                increaseSnakePoint(s);
            }
        }
    }

    private void dead(Snake snake) {
        Message message = new Message(MessageType.DEAD, gson.toJson(snake));
        messageDispatcher.dispatch(message);
    }



    private void increaseSnakePoint(Snake snake) {
        snake.setPoints(snake.getPoints() + 1);
        Message message = new Message(MessageType.KILL, gson.toJson(snake));
        messageDispatcher.dispatch(message);
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
        round++;
        if(round < 3) {
            Message roundOverMessage = new Message(MessageType.ROUND_OVER, null);
            messageDispatcher.dispatch(roundOverMessage);
            resetAllSnakes();
        } else {
            endGame();
        }
    }

    private void resetAllSnakes() {
        for(Snake snake: snakes.values()) {
            snake.resetState();
        }
    }

    private void endGame() {
        Message gameOverMessage = new Message(MessageType.GAME_OVER, null);
        messageDispatcher.dispatch(gameOverMessage);

        for(Snake snake: snakes.values()) {
            removePlayerFromGame(snake.getSessionId());
        }

        resetGame();
    }

    private void resetGame() {
        round = 0;
        gameTimer.cancel();
        gameTimer = new Timer();
    }

    @EventListener
    private void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        removePlayerFromGame(sessionId);
    }
}
