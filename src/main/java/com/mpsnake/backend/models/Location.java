package com.mpsnake.backend.models;

import com.mpsnake.backend.utils.PlayfieldUtils;

import java.util.Objects;

public class Location {
    public int x;
    public int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location getAdjacentLocation(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Location(x, y - PlayfieldUtils.getGridSize());
            case SOUTH:
                return new Location(x, y + PlayfieldUtils.getGridSize());
            case EAST:
                return new Location(x + PlayfieldUtils.getGridSize(), y);
            case WEST:
                return new Location(x - PlayfieldUtils.getGridSize(), y);
            case NONE:
                // fall through
            default:
                return this;
        }
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
