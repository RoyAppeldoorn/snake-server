package com.mpsnake.backend.model;

import com.mpsnake.backend.utils.Playfield;
import com.mpsnake.backend.utils.RandomColorGenerator;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class Snake {

    private static final int DEFAULT_LENGTH = 5;

    private Direction direction;
    private int length = DEFAULT_LENGTH;
    private Location head;
    private final Deque<Location> tail = new ArrayDeque<Location>();

    private final String id;
    private final String hexColor;
    private String username;

    public Snake(String id, String username) {
        this.id = id;
        this.username = username;
        this.hexColor = RandomColorGenerator.getRandomHexColor();
        resetState();
    }

    private void resetState() {
        this.direction = Direction.NONE;
        this.head = Location.getRandomLocation();
        this.tail.clear();
        this.length = DEFAULT_LENGTH;
    }

    public synchronized void update(Collection<Snake> snakes) throws Exception {
        Location nextLocation = head.getAdjacentLocation(direction);
        if (nextLocation.x >= Playfield.getPlayFieldWidth()) {
            nextLocation.x = 0;
        }
        if (nextLocation.y >= Playfield.getPlayFieldHeight()) {
            nextLocation.y = 0;
        }
        if (nextLocation.x < 0) {
            nextLocation.x = Playfield.getPlayFieldWidth();
        }
        if (nextLocation.y < 0) {
            nextLocation.y = Playfield.getPlayFieldHeight();
        }
        if (direction != Direction.NONE) {
            tail.addFirst(head);
            head = nextLocation;
        }

        handleCollisions(snakes);
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

    private synchronized void kill() {
        resetState();
//        String message = "{'type': 'dead'}";
//        messageTemplate.convertAndSend("topic/public", message);
    }

    private synchronized void reward() throws Exception {
        length++;
//        String message = "{'type': 'kill'}";
//        messageTemplate.convertAndSend("topic/public", message);
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
                String.valueOf(id), sb.toString());
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
