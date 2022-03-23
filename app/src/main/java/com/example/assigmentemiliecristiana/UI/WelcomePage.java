package com.example.assigmentemiliecristiana.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assigmentemiliecristiana.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomePage extends AppCompatActivity {

    ImageView homework_icon;
    Timer timer;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        homework_icon = findViewById(R.id.homework_icon);
        homework_icon.setAlpha(0f);
        homework_icon.setTranslationY(50);
        homework_icon.animate().alpha(1f).translationYBy(-50).setDuration(1500);

        timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                Intent intent = new Intent(WelcomePage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        },3500);

    }

}
