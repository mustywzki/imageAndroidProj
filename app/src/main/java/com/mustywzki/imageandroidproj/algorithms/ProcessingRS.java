package com.mustywzki.imageandroidproj.algorithms;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import androidx.appcompat.app.AppCompatActivity;

import com.mustywzki.imageandroidproj.ScriptC_gray;

public class ProcessingRS extends AppCompatActivity{

    public ProcessingRS(){

    }

    public void toGrayRS(Bitmap bmp){
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(this) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap ( rs , bmp ) ;
        Allocation output = Allocation.createTyped ( rs , input.getType()) ;
        // 3) Creer le script
        ScriptC_gray grayScript = new ScriptC_gray(rs);
        // 4) Copier les donnees dans les Allocations
        // ici inutile
        // 5) Initialiser les variables globales potentielles
        // ici inutile
        // 6) Lancer le noyau
        grayScript.forEach_toGray(input, output);
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }
}
