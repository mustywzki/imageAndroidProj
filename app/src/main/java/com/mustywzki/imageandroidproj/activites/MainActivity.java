package com.mustywzki.imageandroidproj.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mustywzki.imageandroidproj.R;
import com.mustywzki.imageandroidproj.algorithms.Processing;
import com.mustywzki.imageandroidproj.algorithms.ProcessingRS;

public class    MainActivity extends AppCompatActivity {

    private enum Processing_type {
        GRAY,
        HUE,
        KEEP_COLOR,
        CONVOLUTION
    }

    private Processing_type currentProcessing;

    // GUI-related members
    private FrameLayout buttonslayout;
    private View button_slide;
    private View slider_bars;
    private TextView sizetext;
    private ImageView imageView;
    private Bitmap currentBmp;
    private Bitmap processedBmp;

    private SeekBar bar1, bar2, bar3;
    private boolean isSliding;

    // RenderScript Scripts member
    private ProcessingRS processingRS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        currentBmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        // User Interface Initialization
        buttonslayout = findViewById(R.id.buttonsLayout);
        button_slide = View.inflate(this,R.layout.buttons_scroller,null);
        slider_bars = View.inflate(this,R.layout.option_sliders,null);
        buttonslayout.addView(button_slide);

        //// Text Setup
        sizetext = findViewById(R.id.sizeText);
        sizetext.setText(currentBmp.getWidth() + "*" + currentBmp.getHeight());

        // Option sliders listeners
        bar1 = slider_bars.findViewById(R.id.seekBar1);
        bar2 = slider_bars.findViewById(R.id.seekBar2);
        bar3 = slider_bars.findViewById(R.id.seekBar3);
        isSliding = false;

        bar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSliding)
                    applyProcessings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSliding = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSliding = false;
            }

        });
        bar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSliding)
                    applyProcessings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSliding = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSliding = false;
            }

        });
        bar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSliding)
                    applyProcessings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSliding = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSliding = false;
            }

        });

        // ProcessingRS initialization
        processingRS = new ProcessingRS();
    }

    // Fonction qui permet de load les seekbars pour être plus efficace (éviter la redondance)
    public void seekbars_load(boolean visible1, String text1, int maxVal1, boolean visible2, String text2, int maxVal2, boolean visible3, String text3, int maxVal3) {
        TextView t1 = slider_bars.findViewById(R.id.textView1), t2 = slider_bars.findViewById(R.id.textView2), t3 = slider_bars.findViewById(R.id.textView3);
        bar1.setVisibility(visible1 ? View.VISIBLE : View.INVISIBLE);
        bar2.setVisibility(visible2 ? View.VISIBLE : View.INVISIBLE);
        bar3.setVisibility(visible3 ? View.VISIBLE : View.INVISIBLE);

        bar1.setMax(maxVal1);
        bar2.setMax(maxVal2);
        bar3.setMax(maxVal3);

        t1.setText(text1);
        t2.setText(text2);
        t3.setText(text3);
    }

    public void applyProcessings(){

        switch (currentProcessing){
            case GRAY:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                Processing.toGray(processedBmp,bar1.getProgress()/100.0,bar2.getProgress()/100.0,bar3.getProgress()/100.0);
                break;
            case HUE:
                processedBmp = currentBmp.copy(currentBmp.getConfig(), true);
                Processing.colorize(processedBmp,bar1.getProgress());
                break;
            case KEEP_COLOR:
                processedBmp = currentBmp.copy(currentBmp.getConfig(),true);
                Processing.keepColor(processedBmp,bar1.getProgress(),bar2.getProgress());

        }
        imageView.setImageBitmap(processedBmp);
    }

    public void onClickProcessings(View v) {
        switch (v.getId()) {
            case R.id.toGray_button:
                currentProcessing = Processing_type.GRAY;
                seekbars_load(true,"Red",100,true,"Green",100,true,"Blue",100);
                // Default bars
                bar1.setProgress(30);
                bar2.setProgress(11);
                bar3.setProgress(59);
                break;
            case R.id.hue_button:
                currentProcessing = Processing_type.HUE;
                seekbars_load(true,"Hue",359,false,"",1,false, "",1);
                break;
            case R.id.chromaKey_button:
                currentProcessing = Processing_type.KEEP_COLOR;
                seekbars_load(true,"Hue",359,true,"Chroma Key",180,false,"",1);
                break;
            case R.id.convolution_button:
                currentProcessing = Processing_type.CONVOLUTION;
                //seekbars_load();
        }
        applyProcessings();

        //Change layout:
        buttonslayout.removeAllViews();
        buttonslayout.addView(slider_bars);
    }

    public void onClickSettings(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                currentProcessing = null;
                buttonslayout.removeAllViews();
                buttonslayout.addView(button_slide);
        }
    }

}
