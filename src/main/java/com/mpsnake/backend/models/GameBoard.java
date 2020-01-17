package com.mpsnake.backend.models;

import java.util.Collection;

public class GameBoard {
    private final int gridSize;
    private final int playfieldWidth;
    private final int playfieldHeight;

    public GameBoard() {
        this.gridSize = 20;
        this.playfieldHeight = 900;
        this.playfieldWidth = 1100;
    }

    public void updateSnakes(Collection<Snake> snakes) throws Exception{
        for (Snake snake : snakes) {
            snake.updatePosition();
            handleCollisions(snakes);
        }
    }

    private void handleCollisions(Collection<Snake> snakes) throws Exception {
        for (Snake snake : snakes) {
            boolean headCollision = id != snake.id && snake.getHead().equals(head);
            boolean tailCollision = snake.getTail().contains(head);
            if (headCollision || tailCollision) {
                kill();
                if (id != snake.id) {
                    snake.reward();
                }
            }
        }
    }



    public void killSnake() {

    }
}
