package com.mpsnake.backend.model;

public class Location {
    public static final int PLAYFIELD_WIDTH = 640;
    public static final int PLAYFIELD_HEIGHT = 480;
    public static final int GRID_SIZE = 10;

    private int x;
    private int y;

    public Location(){}
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
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
}
