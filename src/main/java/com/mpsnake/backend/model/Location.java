package com.mpsnake.backend.model;

import com.mpsnake.backend.utils.Playfield;

import java.util.Objects;
import java.util.Random;

public class Location {
    private static final Random random = new Random();

    public int x;
    public int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location getAdjacentLocation(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Location(x, y - Playfield.getGridSize());
            case SOUTH:
                return new Location(x, y + Playfield.getGridSize());
            case EAST:
                return new Location(x + Playfield.getGridSize(), y);
            case WEST:
                return new Location(x - Playfield.getGridSize(), y);
            case NONE:
                // fall through
            default:
                return this;
        }
    }

    public static Location getRandomLocation() {
        int x = roundByGridSize(random.nextInt(Playfield.getPlayFieldWidth()));
        int y = roundByGridSize(random.nextInt(Playfield.getPlayFieldHeight()));
        return new Location(x, y);
    }

    private static int roundByGridSize(int value) {
        value = value + (Playfield.getGridSize() / 2);
        value = value / Playfield.getGridSize();
        value = value * Playfield.getGridSize();
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
