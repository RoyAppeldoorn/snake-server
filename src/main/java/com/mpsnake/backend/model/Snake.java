package com.mpsnake.backend.model;

import com.mpsnake.backend.utils.SnakeUtils;

public class Snake {

    private final String id;
    private Location head;
    private final String hexColor;
    private String username;

    public Snake(String id, String username) {
        this.id = id;
        this.username = username;
        this.head = SnakeUtils.getRandomLocation();
        this.hexColor = SnakeUtils.getRandomHexColor();
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

    public synchronized String getLocationJson() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{\"x\": %d, \"y\": %d}",
                head.getX(), head.getY()));

        return String.format("{\"id\":\"%s\",\"body\":[%s]}",
                String.valueOf(id), sb.toString());
    }


}
