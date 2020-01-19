package com.mpsnake.backend.models;

import com.mpsnake.backend.utils.PlayfieldUtils;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Location {
    private int x;
    private int y;

    public Location getAdjacentLocation(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Location(x, y - PlayfieldUtils.GRID_SIZE);
            case SOUTH:
                return new Location(x, y + PlayfieldUtils.GRID_SIZE);
            case EAST:
                return new Location(x + PlayfieldUtils.GRID_SIZE, y);
            case WEST:
                return new Location(x - PlayfieldUtils.GRID_SIZE, y);
            default:
                return this;
        }
    }
}
