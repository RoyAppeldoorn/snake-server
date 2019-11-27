package com.mpsnake.backend.model;

import org.springframework.web.socket.WebSocketSession;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Snake {

    private static final int DEFAULT_LENGTH = 5;

    private Direction direction;
    private int length = DEFAULT_LENGTH;
    private Location head;
    private final Deque<Location> tail = new ArrayDeque<Location>();


}
