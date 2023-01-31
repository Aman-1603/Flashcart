package com.example.flashcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RegisterUserActivity extends AppCompatActivity {


    ImageButton loginbackbtn, loginmylocation;
    ImageView profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        loginbackbtn = findViewById(R.id.loginbackbtn);
        loginmylocation = findViewById(R.id.loginlocation);
        profile = findViewById(R.id.profilepic);// remain from here
        

        //back button

        loginbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //detecteing gps

        loginmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here detecting location
            }
        });



    }
}