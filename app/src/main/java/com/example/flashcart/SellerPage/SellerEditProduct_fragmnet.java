package com.example.flashcart.SellerPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flashcart.R;

public class SellerEditProduct_fragmnet extends Fragment {


    public SellerEditProduct_fragmnet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_seller_edit_product_fragmnet, container, false);
        return view;
    }
}