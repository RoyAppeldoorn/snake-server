package com.mpsnake.backend.utils;

import com.mpsnake.backend.models.Location;

import java.util.Random;

public class LocationUtils {

    private static final Random random = new Random();

    public static Location getRandomLocation() {
        int x = roundByGridSize(random.nextInt(PlayfieldUtils.getPlayFieldWidth()));
        int y = roundByGridSize(random.nextInt(PlayfieldUtils.getPlayFieldHeight()));
        return new Location(x, y);
    }

    private static int roundByGridSize(int value) {
        value = value + (PlayfieldUtils.getGridSize() / 2);
        value = value / PlayfieldUtils.getGridSize();
        value = value * PlayfieldUtils.getGridSize();
        return value;
    }
}
