package com.mustywzki.imageandroidproj.algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.RenderScript;

import androidx.renderscript.Allocation;
import androidx.renderscript.ScriptC;

import com.mustywzki.imageandroidproj.ScriptC_gray;

public class Processing {

    public static void toGray(Bitmap bmp, double red, double green, double blue){

        // Scaling RGB values to the specific range (equalizer)
        red = red > 1.0 ? 1.0 : red;
        red = red < 0.0 ? 0.0 : red;

        blue = blue > 1.0 ? 1.0 : blue;
        blue = blue < 0.0 ? 0.0 : blue;

        green = green > 1.0 ? 1.0 : green;
        green = green < 0.0 ? 0.0 : green;

        // Copying the bitmap bmp's pixels into a int[] in order to perform the algorithm faster using getPixels()
        int[] tmpCopy = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(tmpCopy, 0, bmp.getWidth(), 0,0,bmp.getWidth(),bmp.getHeight());

        // Applying gray filter
        for(int i = 0; i < tmpCopy.length; i++) {
                int currentPixel = tmpCopy[i];
                int grayValue = (int)(red * (double) Color.red(currentPixel) + blue * (double) Color.blue(currentPixel) + green * (double) Color.green(currentPixel));
                tmpCopy[i] = Color.rgb(grayValue,grayValue,grayValue);
        }
        bmp.setPixels(tmpCopy,0, bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    public static void colorize(Bitmap bmp, float hue) {

        // Copying the bitmap bmp's pixels into a int[] in order to perform the algorithm faster using getPixels()
        int[] tmpCopy = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        // Applying hue modifications
        for (int i = 0; i < tmpCopy.length; i++) {
            int currentPixel = tmpCopy[i];
            float[] hsv = new float[3];
            Utils.RGBToHSV(Color.red(currentPixel), Color.green(currentPixel), Color.blue(currentPixel), hsv); // Getting HSV values for the currentPixel
            hsv[0] = hue; // setting up the new hue selected
            tmpCopy[i] = Utils.HSVToColor(hsv, Color.alpha(currentPixel)); // Setting the HSV values back to RGB in order to set the modified pixel
        }
        bmp.setPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

}
