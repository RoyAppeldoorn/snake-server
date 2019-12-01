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

    private final int id;
    private final SessionConnectedEvent session;
    private Location head;

    private int x;
    private int y;

    public Snake(int id, SessionConnectedEvent session) {
        this.id = id;
        this.session = session;
        this.head = SnakeUtils.getRandomLocation();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public synchronized Location getHead() {
        return head;
    }

    public int getId() {
        return id;
    }

    public synchronized String getLocationsJson() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{x: %d, y: %d}",
                Integer.valueOf(head.getX()), Integer.valueOf(head.getY())));

        return String.format("{'id':%d,'body':[%s]}",
                Integer.valueOf(id), sb.toString());
    }
}
