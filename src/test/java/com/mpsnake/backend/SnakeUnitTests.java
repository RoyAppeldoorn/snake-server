package com.mpsnake.backend;

import com.mpsnake.backend.models.Direction;
import com.mpsnake.backend.models.Location;
import com.mpsnake.backend.models.Snake;
import com.mpsnake.backend.utils.PlayfieldUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SnakeUnitTests {

    @Test
    public void initializeSnake() {
        // Arrange
        Snake snake = new Snake("test", "test2", "test3");

        // Act

        // Assert
        Assert.assertNotNull(snake.getHexColor());
        Assert.assertNotNull(snake.getUuid());
        Assert.assertNotNull(snake.getSessionId());
        Assert.assertNotNull(snake.getNickname());
    }

    @Test
    public void resetSnakeState() {
        // Arrange
        Snake snake = new Snake("test", "test2", "test3");

        // Act
        snake.setDirection(Direction.EAST);
        snake.setPoints(2);
        snake.getTail().add(new Location(snake.getHead().getX() + 20, snake.getHead().getY()));

        // Assert
        Assert.assertTrue(snake.getDirection() == Direction.EAST);
        Assert.assertTrue(snake.getPoints() == 2);
        Assert.assertTrue(snake.getTail().size() == 1);

        snake.resetState();

        Assert.assertTrue(snake.getPoints() == 0);
        Assert.assertTrue(snake.getTail().size() == 0);
    }

    @Test
    public void updateSnakePosition() {
        // Arrange
        Snake snake = new Snake("test", "test2", "test3");
        Location currentLocation = snake.getHead();

        // Act
        snake.setDirection(Direction.EAST);
        snake.updatePosition();
        Location newLocation = snake.getHead();

        // Assert
        Assert.assertNotEquals(newLocation, currentLocation);
    }

    @Test
    public void returnOppositeXLocation() {
        // Arrange
        Snake snake = new Snake("test", "test2", "test3");

        // Act
        snake.getHead().setX(1100);
        snake.setDirection(Direction.EAST);
        snake.updatePosition();

        // Assert
        Assert.assertEquals(0, snake.getHead().getX());
    }

    @Test
    public void returnOppositeYLocation() {
        // Arrange
        Snake snake = new Snake("test", "test2", "test3");

        // Act
        snake.getHead().setY(PlayfieldUtils.PLAYFIELD_HEIGHT);
        snake.setDirection(Direction.SOUTH);
        snake.updatePosition();

        // Assert
        Assert.assertEquals(0, snake.getHead().getY());
    }

    @Test
    public void updateSnakeTaleAfterSnakesMoves() {
        // Arrange
        Snake snake = new Snake("test", "test2", "test3");

        // Act
        snake.getHead().setY(PlayfieldUtils.PLAYFIELD_HEIGHT);
        snake.setDirection(Direction.NORTH);
        snake.updatePosition();

        // Assert
        Assert.assertEquals(1, snake.getTail().size());
    }
}
