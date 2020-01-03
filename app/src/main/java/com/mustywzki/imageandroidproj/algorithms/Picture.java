package com.mustywzki.imageandroidproj.algorithms;

import android.graphics.Bitmap;

public class Picture {

    private Bitmap bmp;


    public Picture(Bitmap bmp){
     this.bmp = bmp;
    }


    public int getWidth(){
        return bmp.getWidth();
    }

    public int getHeight() {
        return bmp.getHeight();
    }
}
