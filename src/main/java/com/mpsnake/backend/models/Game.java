package com.mpsnake.backend.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private static int ROUND_COUNT = 0;
    private static int MAX_ROUND_COUNT = 3;
    private Timer gameTimer = null;
    private final static int TICK_DELAY = 200;

    public enum Status {
        WAITING, IN_PROGRESS
    }

    private Status status;
    private int round;

    private final GameBoard gameBoard;
    private final ConcurrentHashMap<String, Snake> snakes;
    private final Log log = LogFactory.getLog(Game.class);

    public Game() {
        this.round = ROUND_COUNT;
        this.status = Status.WAITING;
        this.gameBoard = new GameBoard();
        this.gameTimer = new Timer(Game.class.getSimpleName() + " Timer");
        snakes = new ConcurrentHashMap<String, Snake>();
    }

    public void addPlayerToGame(Snake snake) {
        snakes.put(String.valueOf(snake.getId()), snake);
        if(snakes.size() == 2) {
            status = Status.IN_PROGRESS;
        }
    }

    public void removePlayerFromGame(String id) {
        snakes.remove(id);
    }

    public void startGame() {
        increaseRoundCount();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    gameBoard.updateSnakes(getSnakes());
                } catch (Throwable e) {
                    log.error("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    private void increaseRoundCount() {
        ROUND_COUNT++;
    }

    public Collection<Snake> getSnakes() {
        return Collections.unmodifiableCollection(snakes.values());
    }

    public Status getStatus() {
        return status;
    }
}
