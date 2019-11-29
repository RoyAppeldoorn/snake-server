package com.mpsnake.backend.model;

import org.springframework.web.socket.WebSocketSession;
import com.mpsnake.backend.utils.SnakeUtils;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

public class Snake {

    private static final int DEFAULT_LENGTH = 5;

    private final int id;
    private final SessionConnectedEvent session;

    private Direction direction;
    private int length = DEFAULT_LENGTH;
    private Location head;
    private final Deque<Location> tail = new ArrayDeque<Location>();
    private final String hexColor;

    public Snake(int id, SessionConnectedEvent session) {
        this.id = id;
        this.session = session;
        this.hexColor = SnakeUtils.getRandomHexColor();
        resetState();
    }

    private void resetState() {
        this.direction = Direction.NONE;
        this.head = SnakeUtils.getRandomLocation();
        this.tail.clear();
        this.length = DEFAULT_LENGTH;
    }

    public synchronized Location getHead() {
        return head;
    }

    public synchronized Collection<Location> getTail() {
        return tail;
    }

    public int getId() {
        return id;
    }

}
