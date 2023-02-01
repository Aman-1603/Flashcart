package com.example.flashcart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterSellerActivity extends AppCompatActivity {


    ImageButton sellerbackbtn, sellermylocation;
    ImageView sellerprofile;

    EditText email,pass,name,phone,country,state,city;
    Button sellerregisterbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sellerbackbtn = findViewById(R.id.sellerbackbtn);
        sellermylocation = findViewById(R.id.sellerlocation);
        sellerprofile = findViewById(R.id.sellerprofilepic);
        email = findViewById(R.id.sellerEmail);
        name = findViewById(R.id.sellerName);
        pass = findViewById(R.id.sellerpassword);
        phone = findViewById(R.id.sellerPhone);
        country = findViewById(R.id.sellercountry);
        state = findViewById(R.id.sellerstate);
        city = findViewById(R.id.sellercity);
        sellerregisterbtn = findViewById(R.id.sellerregisterbtn);





        sellerregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //seller register here
            }
        });

    }
}