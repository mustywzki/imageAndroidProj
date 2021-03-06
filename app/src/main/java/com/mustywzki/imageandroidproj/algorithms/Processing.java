package com.mustywzki.imageandroidproj.algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

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
            tmpCopy[i] = Utils.HSVToRGB(hsv, Color.alpha(currentPixel)); // Setting the HSV values back to RGB in order to set the modified pixel
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
            tmpCopy[i] = Utils.HSVToRGB(hsvValues, Color.alpha(currentPixel));
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

        int multiplier = dimension*dimension;
        int halfrange = dimension/2;

        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        int[] tmpPix = pixels.clone();

        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int x = 0 + halfrange; x < bmp.getWidth()-halfrange; x++){
            for (int y = 0 + halfrange; y < bmp.getHeight()-halfrange; y++) {
                red = 0;
                green = 0;
                blue = 0;
                for (int i = x-halfrange; i <= x+halfrange; i++) {
                    for (int j = y-halfrange; i <= y+halfrange; j++){
                        red += Color.red(pixels[i+j*bmp.getHeight()]);
                        green += Color.green(pixels[i+j*bmp.getHeight()]);
                        blue += Color.blue(pixels[i+j*bmp.getHeight()]);
                    }
                }
                red /= multiplier;
                green /= multiplier;
                blue /= multiplier;

                tmpPix[x+y*bmp.getHeight()] = Color.rgb(red,green,blue);
            }
        }
        bmp.setPixels(tmpPix, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public static void computeDynamicLinearExtension(int[] histogram, Bitmap bmp){
        int i = 0;
        int min, max;
        while (histogram[i] == 0)
        {
            i++;
        }
        min = i;

        i = histogram.length - 1;
        while (histogram[i] == 0){
            i--;
        }

        max = i;

        if (max == min){
            return;
        }

        int[] LUT = new int[histogram.length]; //LOOK UP TABLE
            for(int ng = 0; ng < histogram.length; ng++){
            LUT[ng] = (255*(ng- min))/(max-min);
        }

        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (i = 0; i < bmp.getWidth()*bmp.getHeight();i++){
            int px = pixels[i];
            float[] hsv = new float[3];
            Utils.RGBToHSV(Color.red(px), Color.green(px), Color.blue(px), hsv);
            hsv[2] = (float) LUT[(int) (hsv[2] * 255f)] / 255f;
            pixels[i] = Utils.HSVToRGB(hsv, Color.alpha(px));
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    public static void histogramEqualizer(int[] hist, Bitmap bmp){
        int[] cumulativeHist;
        cumulativeHist = Utils.cumulativeHistogram(hist);

        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int i = 0; i < bmp.getHeight()*bmp.getWidth(); i++){
            int px = pixels[i];
            float[] hsv = new float[3];
            Utils.RGBToHSV(Color.red(px), Color.green(px), Color.blue(px), hsv);
            hsv[2] = (float) (cumulativeHist[(int) (hsv[2] * 255f)] * 255 / pixels.length) / 255f;
            pixels[i] = Utils.HSVToRGB(hsv, Color.alpha(px));
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

}
