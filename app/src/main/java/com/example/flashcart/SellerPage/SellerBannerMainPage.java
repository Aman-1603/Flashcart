package com.example.flashcart.SellerPage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.flashcart.R;
import com.example.flashcart.databinding.ActivitySellerBannerMainPageBinding;

public class SellerBannerMainPage extends AppCompatActivity {

    ActivitySellerBannerMainPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_banner_main_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding = ActivitySellerBannerMainPageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());




        replaceFragment(new SellerBannerProduct_fragmnet());

        binding.bottomnavigationBar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.uploadbanner:
                    replaceFragment(new SellerBannerProduct_fragmnet());
                    break;
                case R.id.AllBanner:
                    replaceFragment(new SellerAddProduct_fragment());

                    break;
            }
            return true;
        });


    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_layout,fragment);
        fragmentTransaction.commit();
    }
}