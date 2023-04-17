package com.example.flashcart.SellerPage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.flashcart.R;


public class SellerPromotionCodeFragment extends Fragment {



    ImageView addpromotioncodebtn;

    public SellerPromotionCodeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_promotion_code, container, false);

        addpromotioncodebtn = view.findViewById(R.id.addpromotioncodebtn);

        addpromotioncodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigatetoADDpromotionFragmnet();

            }
        });



        return view;
    }

    private void navigatetoADDpromotionFragmnet() {

        Seller_Add_PromotionCode_fragmnet newFragment = new Seller_Add_PromotionCode_fragmnet();


        // Get the fragment manager
        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        // Begin a new transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the previous fragment with the new fragment
        fragmentTransaction.replace(R.id.Frame_layout, newFragment);

        // Add the transaction to the back stack so the user can navigate back to the previous fragment
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();


    }
}