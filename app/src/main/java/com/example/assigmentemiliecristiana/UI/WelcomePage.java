package com.example.assigmentemiliecristiana.UI;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assigmentemiliecristiana.R;

public class WelcomePage extends AppCompatActivity {

    ImageView homework_icon;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        homework_icon = findViewById(R.id.homework_icon);

        homework_icon.setAlpha(0f);
        homework_icon.setTranslationY(50);

        homework_icon.animate().alpha(1f).translationYBy(-50).setDuration(1500);

    }

}
