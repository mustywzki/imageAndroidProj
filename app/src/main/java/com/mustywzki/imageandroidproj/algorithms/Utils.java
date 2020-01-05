package com.mustywzki.imageandroidproj.algorithms;

import android.graphics.Color;

public class Utils {

    public static int HSVToColor(float[] hsv, int alpha) {
        int t = (int) ((hsv[0] / 60f) % 6);
        float C = hsv[1] * hsv[2];
        float X = C * (1 - Math.abs(((hsv[0] / 60) % 2) - 1));
        float m = hsv[2] - C;

        float r = 0, g = 0, b = 0;

        switch (t) {
            case 0:
                r = C;
                g = X;
                b = 0;
                break;
            case 1:
                r = X;
                g = C;
                b = 0;
                break;
            case 2:
                r = 0;
                g = C;
                b = X;
                break;
            case 3:
                r = 0;
                g = X;
                b = C;
                break;
            case 4:
                r = X;
                g = 0;
                b = C;
                break;
            case 5:
                r = C;
                g = 0;
                b = X;
                break;
        }
        float new_r = (r + m) * 255, new_g = (g + m) * 255, new_b = (b + m) * 255;
        return Color.argb(alpha, (int) new_r, (int) new_g, (int) new_b);
    }

    public static void RGBToHSV(int red, int green, int blue, float[] hsv) { // still unsure whether it's actually working: https://en.wikipedia.org/wiki/HSL_and_HSV
        float r = red / 255f, g = green / 255f, b = blue / 255f;

        float max = Math.max(Math.max(r, g), b), min = Math.min(Math.min(r, g), b);

        if (max == min) {
            hsv[0] = 0;
        } else if (max == r) {
            hsv[0] = (60 * ((g - b) / (max - min)) + 360);
            while (hsv[0] > 360) hsv[0] -= 360;
        } else if (max == g) {
            hsv[0] = (60 * ((b - r) / (max - min)) + 120);
        } else if (max == b) {
            hsv[0] = (60 * ((r - g) / (max - min)) + 240);
        }

        if (max == 0) {
            hsv[1] = 0;
        } else {
            hsv[1] = 1 - (min / max);
        }

        hsv[2] = max;

    }


}
