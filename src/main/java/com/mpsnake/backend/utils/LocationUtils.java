package com.mpsnake.backend.utils;

import com.mpsnake.backend.models.Location;

import java.util.Random;

public class LocationUtils {

    private static final Random random = new Random();

    public static Location getRandomLocation() {
        int x = roundByGridSize(random.nextInt(PlayfieldUtils.PLAYFIELD_WIDTH));
        int y = roundByGridSize(random.nextInt(PlayfieldUtils.PLAYFIELD_HEIGHT));
        return new Location(x, y);
    }

    private static int roundByGridSize(int value) {
        value = value + (PlayfieldUtils.GRID_SIZE / 2);
        value = value / PlayfieldUtils.GRID_SIZE;
        value = value * PlayfieldUtils.GRID_SIZE;
        return value;
    }
}
