package com.example.flashcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcart.profilePage.ProfileActivitySeller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainsellerActivity extends AppCompatActivity {


    private TextView nameTV,shopnameTV,emailTV;
     ImageButton logoutbtn,profile;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainseller);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        nameTV = findViewById(R.id.nametv);
        shopnameTV = findViewById(R.id.shopanmetv);
        emailTV = findViewById(R.id.emailtv);
        logoutbtn = findViewById(R.id.logoutbtn);


        firebaseAuth = FirebaseAuth.getInstance();
        CheckUser();

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CheckUser();
                startActivity(new Intent(MainsellerActivity.this, ProfileActivitySeller.class));
            }
        });



    }

    private void CheckUser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainsellerActivity.this,LoginActivity.class));
            finish();
        }else{
            loadMyInfo();
        }
    }

    private void loadMyInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot ds : datasnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String shopname = ""+ds.child("shopName").getValue();
                            String email = ""+ds.child("email").getValue();


                            nameTV.setText(name);
                            shopnameTV.setText(shopname);
                            emailTV.setText(email);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



}