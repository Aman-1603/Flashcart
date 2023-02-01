package com.example.flashcart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText loginemail, loginpassword;
    TextView loginforgot, loginnoaccount;
    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //removing appbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginemail = findViewById(R.id.loginEmail);
        loginpassword = findViewById(R.id.loginPass);
        loginforgot = findViewById(R.id.loginforgot);
        loginbtn = findViewById(R.id.loginbtn);
        loginnoaccount = findViewById(R.id.loginNoAc);

        loginnoaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterUserActivity.class));
            }
        });

        loginforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Forgot_accpassActivity.class));
            }
        });


    }
}