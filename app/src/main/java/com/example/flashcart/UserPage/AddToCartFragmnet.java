package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flashcart.R;


public class AddToCartFragmnet extends Fragment {


    public AddToCartFragmnet() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_cart_fragmnet, container, false);
        return view;
    }
}