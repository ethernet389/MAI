package com.example.mai2.main_programme.algorithm.color;

import android.graphics.Color;

public class ColorGenerator {
    private final int alpha;
    private final int[] startRGB, endRGB;

    private final int[] colorOffsets;

    private void parseRGB(int[] rgb, int color){
        rgb[0] = Color.red(color);
        rgb[1] = Color.green(color);
        rgb[2] = Color.blue(color);
    }

    public ColorGenerator(int amountOfColors, int startARGB, int endARGB){
        colorOffsets = new int[3];
        startRGB = new int[3];
        endRGB = new int[3];

        alpha = Color.alpha(startARGB);
        parseRGB(startRGB, startARGB);
        parseRGB(endRGB, endARGB);

        for (int i = 0; i < startRGB.length; ++i){
            colorOffsets[i] = Math.abs(startRGB[i] - endRGB[i]) / amountOfColors;
            if (startRGB[i] > endRGB[i]){
                colorOffsets[i] *= -1;
            }
        }
    }

    public int getColorOfNumber(int number) {
        --number;
        int[] rgb = new int[3];
        for (int i = 0; i < rgb.length; ++i){
            rgb[i] = startRGB[i] + number * colorOffsets[i];
        }

        return Color.argb(alpha, rgb[0], rgb[1], rgb[2]);
    }
}
