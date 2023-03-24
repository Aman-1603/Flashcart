package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flashcart.R;


public class UserOrderDetailFragment extends Fragment {


    String orderTo,orderId;


    public UserOrderDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_order_detail, container, false);



        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            orderTo = bundle.getString("orderTo");
            orderId = bundle.getString("orderId");
        }


        return view;
    }
}