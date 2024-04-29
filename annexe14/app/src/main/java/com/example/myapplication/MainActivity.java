package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout principale,Colonne1,Colonne2,Colonne3, Colonne4, colonne;

    ImageView jeton1, jeton2,jeton3,jeton4;

    View jeton = null;

    Ecouteur ec;
    EcouteurTouch ect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        principale = findViewById(R.id.principale);

        ec = new Ecouteur();
        ect = new EcouteurTouch();

        for (int i = 0 ; i < principale.getChildCount(); i++){
            LinearLayout colonne = (LinearLayout) principale.getChildAt(i);
            colonne.setOnDragListener(ect);
            colonne.getChildAt(0).setOnTouchListener(ect);
        }

    }

    private class EcouteurTouch implements View.OnTouchListener,View.OnDragListener {

        @Override
        public boolean onTouch(View source, MotionEvent event) {
            // source = imageView / jeton à bouger
            // C'est l'ombre qu'on va transporter d'une colonne a l'autre
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(source);

            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion <= 24){
                source.startDrag(null,shadowBuilder,source,0);
            }
            else
                source.startDragAndDrop(null,shadowBuilder,source,0);

            // on set l'object que l'on drag en mode invisible
            source.setVisibility(View.INVISIBLE);
            return true;
        }

        @Override
        public boolean onDrag(View source, DragEvent event) {
            // source = LinearLayout / colonne
            switch(event.getAction()){

                case DragEvent.ACTION_DRAG_ENTERED:
                    source.setBackground(getDrawable((R.drawable.background_contenant_selectionne)));
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    source.setBackground(getDrawable((R.drawable.background_contenant)));
                    break;

                case DragEvent.ACTION_DROP:
                    //le jeton resté sur la colonne de départ (Paramètre de startDragAnd Drop)
                    jeton = (View)event.getLocalState();
                    // aller chercher le conteneur d'origine
                    LinearLayout parent = (LinearLayout)jeton.getParent();
                    // enlever le jeton du cotneneur d'origine (car on l'avait seulement rendu invisible)
                    parent.removeView(jeton);
                    // colonne ou je suis rendu
                    LinearLayout conteneur = (LinearLayout)source;
                    // ajouter le jeton
                    conteneur.addView(jeton);
                    // remet le jeton visible
                    jeton.setVisibility(View.VISIBLE);
                    break;

            }

            return true;
        }
    }

    private class Ecouteur implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            return false;
        }
    }
}