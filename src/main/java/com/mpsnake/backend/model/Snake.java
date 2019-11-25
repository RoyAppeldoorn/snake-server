package com.mpsnake.backend.model;

import java.awt.*;
import java.util.ArrayList;

public class Snake {
    public int id;
    public String name;
    private byte direction;
    private Point location;
    private int tailLength;
    private ArrayList<Point> tail;

    public Snake(String name, byte direction, Point locations) {
        this.name = name;
        this.direction = direction;
        this.location = locations;
    }

    public Point getLocation() {
        return new Point(this.location);
    }
}
