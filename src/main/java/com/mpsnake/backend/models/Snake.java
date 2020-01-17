package com.mpsnake.backend.models;

import com.mpsnake.backend.utils.LocationUtils;
import com.mpsnake.backend.utils.PlayfieldUtils;
import com.mpsnake.backend.utils.RandomColorGenerator;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class Snake {

    private Direction direction;
    private Location head;
    private final Deque<Location> tail;

    private final String id;
    private final String hexColor;
    private String username;

    public Snake(String id, String username) {
        this.id = id;
        this.username = username;
        this.hexColor = RandomColorGenerator.getRandomHexColor();
        this.tail = new ArrayDeque<Location>();
        resetState();
    }

    public void resetState() {
        this.direction = Direction.NONE;
        this.head = LocationUtils.getRandomLocation();
        this.tail.clear();
    }

    public synchronized void updatePosition() {
        Location nextLocation = head.getAdjacentLocation(direction);
        if (nextLocation.x >= PlayfieldUtils.getPlayFieldWidth()) {
            nextLocation.x = 0;
        }
        if (nextLocation.y >= PlayfieldUtils.getPlayFieldHeight()) {
            nextLocation.y = 0;
        }
        if (nextLocation.x < 0) {
            nextLocation.x = PlayfieldUtils.getPlayFieldWidth();
        }
        if (nextLocation.y < 0) {
            nextLocation.y = PlayfieldUtils.getPlayFieldHeight();
        }
        if (direction != Direction.NONE) {
            tail.addFirst(head);
            head = nextLocation;
        }
    }

    public synchronized boolean checkCollisionWithOtherSnakes(Snake snake) {
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

    public synchronized String getLocationJson() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{\"x\": %d, \"y\": %d}",
                Integer.valueOf(head.x), Integer.valueOf(head.y)));
        for (Location location : tail) {
            sb.append(',');
            sb.append(String.format("{\"x\": %d, \"y\": %d}",
                    Integer.valueOf(location.x), Integer.valueOf(location.y)));
        }
        return String.format("{\"id\":\"%s\",\"body\":[%s]}",
                id, sb.toString());
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHexColor() {
        return hexColor;
    }

    public synchronized Location getHead() {
        return head;
    }

    public synchronized Collection<Location> getTail() {
        return tail;
    }

    public synchronized void setDirection(Direction direction) {
        this.direction = direction;
    }
}
