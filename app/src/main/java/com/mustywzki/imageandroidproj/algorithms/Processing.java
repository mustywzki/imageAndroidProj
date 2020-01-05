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

    public static void keepColor(Bitmap bmp, float hue, float chromakey) {
        hue = hue % 360f;
        chromakey = chromakey % 180f;
        int[] tmpCopy = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        for (int i = 0; i < tmpCopy.length; i++) {
            int currentPixel = tmpCopy[i];
            float[] hsvValues = new float[3];
            Utils.RGBToHSV(Color.red(currentPixel), Color.green(currentPixel), Color.blue(currentPixel), hsvValues);
            float diff = Math.abs(hsvValues[0] - hue);
            if (!(Math.min(diff, 360 - diff) <= chromakey)) {
                hsvValues[1] = 0;
            }
            tmpCopy[i] = Utils.HSVToColor(hsvValues, Color.alpha(currentPixel));
        }
        bmp.setPixels(tmpCopy, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public static void convolution(Bitmap bmp, int dimension) {

        int red;
        int green;
        int blue;

        if (dimension%2 == 0)
        {
            return;
        }

        int multiplier = dimension * dimension;
        int halfrange = (dimension/2) - 1;

        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        int[] tmpPix = pixels.clone();

        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int x = 0 + halfrange; x < bmp.getWidth()-halfrange; x++){
            for (int y = 0 + halfrange; y < bmp.getHeight()-halfrange; y++) {
                red = 0;
                green = 0;
                blue = 0;
                for (int i = x-halfrange; i < x+halfrange; i++) {
                    for (int j = y-halfrange; i < y+halfrange; j++){
                        red += Color.red(pixels[i+j*bmp.getHeight()]);
                        green += Color.green(pixels[i+j*bmp.getHeight()]);
                        blue += Color.blue(pixels[i+j*bmp.getHeight()]);
                    }
                }
                tmpPix[x+y*bmp.getHeight()] /= dimension;
            }
        }
        bmp.setPixels(tmpPix, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }
}
