package com.mpsnake.backend.model;

import java.util.Objects;
import java.util.Random;

public class Location {
    public static final int GRID_SIZE = 20;
    public static final int PLAYFIELD_WIDTH = 1100;
    public static final int PLAYFIELD_HEIGHT = 900;

    private static final Random random = new Random();

    public int x;
    public int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

//    public int getX() {
//        return x;
//    }
//
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public void setY(int y) {
//        this.y = y;
//    }

    public Location getAdjacentLocation(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Location(x, y - GRID_SIZE);
            case SOUTH:
                return new Location(x, y + GRID_SIZE);
            case EAST:
                return new Location(x + GRID_SIZE, y);
            case WEST:
                return new Location(x - GRID_SIZE, y);
            case NONE:
                // fall through
            default:
                return this;
        }
    }

    public static Location getRandomLocation() {
        int x = roundByGridSize(random.nextInt(PLAYFIELD_WIDTH));
        int y = roundByGridSize(random.nextInt(PLAYFIELD_HEIGHT));
        return new Location(x, y);
    }

    private static int roundByGridSize(int value) {
        value = value + (GRID_SIZE / 2);
        value = value / GRID_SIZE;
        value = value * GRID_SIZE;
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x &&
                y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
