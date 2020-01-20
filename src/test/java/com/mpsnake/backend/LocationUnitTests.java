package com.mpsnake.backend;

import com.mpsnake.backend.models.Direction;
import com.mpsnake.backend.models.Location;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class LocationUnitTests {
    // <editor-fold defaultstate="collapsed" desc="Location tests">
    @Test
    public void getNewAdjacentLocation() {
        // Arrange
        Location location = new Location(100, 100);

        // Act
        Direction direction = Direction.EAST;
        Location newLocation = location.getAdjacentLocation(direction);

        // Assert
        Assert.assertTrue(newLocation.getX() == 120);
    }
    // </editor-fold>
}
