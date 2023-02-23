package com.example.flashcart.SellerPage;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.flashcart.R;
import com.example.flashcart.SellerPage.SellerEditProduct_fragmnet;
import com.example.flashcart.SellerPage.SellerHomePage1_fragment;
import com.example.flashcart.databinding.ActivityMainsellerBinding;
import com.example.flashcart.databinding.ActivitySellerEditMainPageBinding;

public class SellerEditMainPage extends AppCompatActivity {

    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_edit_main_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        productId = getIntent().getStringExtra("ProductId");




    }



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_layouteditSellerMain,fragment);
        fragmentTransaction.commit();
    }
}