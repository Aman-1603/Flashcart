package com.example.flashcart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class splashActivity extends AppCompatActivity {


    ImageView logo;
    LottieAnimationView splashimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        logo = findViewById(R.id.logo);
        splashimg = findViewById(R.id.lottie);

        Intent intent = new Intent(this, LoginActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 2000);
    }


}