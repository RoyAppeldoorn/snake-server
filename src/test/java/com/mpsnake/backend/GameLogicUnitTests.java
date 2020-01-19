package com.mpsnake.backend;

import com.mpsnake.backend.logic.Game;
import com.mpsnake.backend.models.Direction;
import com.mpsnake.backend.models.Snake;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameLogicUnitTests {

    @Autowired
    Game game;

    @Test
    public void addSnakeToGame() {
        // Act
        Snake snek = new Snake("test", "test2", "test3");
        game.addPlayerToGame(snek);

        // Assert
        Assert.assertEquals(1, game.getSnakes().size());
    }

    @Test
    public void removeSnakeFromGame() {
        // Arrange
        Snake snek = new Snake("test", "test2", "test3");

        // Act
        game.addPlayerToGame(snek);
        Assert.assertEquals(1, game.getSnakes().size());

        // Assert
        game.removePlayerFromGame(snek.getSessionId());
        Assert.assertEquals(0, game.getSnakes().size());
    }

    @Test
    public void setSnakeDirection() {
        // Arrange
        Snake snek = new Snake("test", "test2", "test3");

        // Act
        game.addPlayerToGame(snek);
        game.setDirection(snek.getSessionId(), Direction.NORTH);
        Direction updatedDirection = game.getSnakes().get(snek.getSessionId()).getDirection();

        // Assert
        Assert.assertEquals(Direction.NORTH, updatedDirection);
    }
}
