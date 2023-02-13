package com.example.flashcart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.flashcart.SellerPage.SellerAddProduct_fragment;
import com.example.flashcart.SellerPage.SellerHomePage1_fragment;
import com.example.flashcart.databinding.ActivityMainUserBinding;
import com.example.flashcart.databinding.ActivityMainsellerBinding;

public class MainsellerActivity extends AppCompatActivity {


    ActivityMainsellerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainseller);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding = ActivityMainsellerBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //bydefault fragment would be home page of use

        replaceFragment(new SellerHomePage1_fragment());

        binding.bottomnavigationBarseller.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.Home:
                    replaceFragment(new SellerHomePage1_fragment());
                    break;
                case R.id.Products:
                    replaceFragment(new SellerAddProduct_fragment());

                    break;
                case R.id.Notification:
                    break;
                case R.id.Orders:
                    break;
                case R.id.Account:
                    break;

            }
            return true;
        });

//        logoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //CheckUser();
//                startActivity(new Intent(MainsellerActivity.this, ProfileActivitySeller.class));
//            }
//        });



    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_layout,fragment);
        fragmentTransaction.commit();
    }


}