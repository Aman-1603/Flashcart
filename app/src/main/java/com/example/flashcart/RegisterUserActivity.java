package com.example.flashcart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterUserActivity extends AppCompatActivity {


    ImageButton loginbackbtn, loginmylocation;
    ImageView profile;

    EditText email,pass,name,phone,country,state,city;
    Button registerbtn;
    TextView registerseller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //removing appbar

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginbackbtn = findViewById(R.id.loginbackbtn);
        loginmylocation = findViewById(R.id.loginlocation);
        profile = findViewById(R.id.profilepic);
        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginPass);
        name = findViewById(R.id.loginName);
        phone = findViewById(R.id.loginPhone);
        country = findViewById(R.id.logincountry);
        state = findViewById(R.id.loginstate);
        city = findViewById(R.id.logincity);
        registerbtn = findViewById(R.id.registerbtn);
        registerseller = findViewById(R.id.areyouseller);



        //back button

        loginbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // are you seller

        registerseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUserActivity.this,RegisterSellerActivity.class));
            }
        });

        //detecteing gps

        loginmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here detecting location
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
            }
        });


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //register the user here

            }
        });



    }
}