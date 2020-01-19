package com.mpsnake.backend.utils;

import java.awt.*;
import java.security.SecureRandom;
import java.util.Random;

public class RandomColorGenerator {

    private final static SecureRandom random = new SecureRandom(); // Compliant for security-sensitive use cases

    public static String getRandomHexColor() {
        float hue = random.nextFloat();
        // sat between 0.1 and 0.3
        float saturation = (random.nextInt(2000) + 1000) / 10000f;
        float luminance = 0.9f;
        Color color = Color.getHSBColor(hue, saturation, luminance);
        return '#' + Integer.toHexString(
                (color.getRGB() & 0xffffff) | 0x1000000).substring(1);
    }

}
