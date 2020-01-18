package com.mpsnake.backend.models;

import com.mpsnake.backend.utils.PlayfieldUtils;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Location {
    public int x;
    public int y;

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
}
