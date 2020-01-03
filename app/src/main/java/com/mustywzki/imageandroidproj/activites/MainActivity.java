package com.mustywzki.imageandroidproj.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mustywzki.imageandroidproj.R;
import com.mustywzki.imageandroidproj.algorithms.Picture;

public class    MainActivity extends AppCompatActivity {

    // GUI-related members
    private FrameLayout buttonslayout;
    private View button_slide;
    private TextView sizetext;
    private Picture picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView imageView = findViewById(R.id.imageView);
        Bitmap bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        picture = new Picture(bmp);

        // User Interface Initialization
        buttonslayout = findViewById(R.id.buttonsLayout);
        button_slide = View.inflate(this,R.layout.button_slide,null);
        buttonslayout.addView(button_slide);

        //// Text Setup
        sizetext = findViewById(R.id.sizeText);
        sizetext.setText(picture.getWidth() + "*" + picture.getHeight());
    }
}
