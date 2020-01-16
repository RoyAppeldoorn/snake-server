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

    public void updateSnakes(Collection<Snake> snakes) {
        for (Snake snake : snakes) {
            snake.updatePosition();
            if(snake.checkCollisionWithOtherSnake(snake)) {

            }
        }
    }

    public void killSnake() {

    }
}
