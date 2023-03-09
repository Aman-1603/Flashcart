package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;


public class UserProductPage extends Fragment {


    TextView productTitleTV,productTitle1TV,ProductPriceTV,product_descriptionTV,addressTV,cityTV,StateTV,CountryTV,PhoneTV;

    String ItemUid;
    private FirebaseAuth firebaseAuth;

    public UserProductPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_product_page, container, false);



//        productTitleTV = view.findViewById(R.id.product_title);
//        productTitle1TV = view.findViewById(R.id.productTitle1TV);
//        ProductPriceTV = view.findViewById(R.id.ProductPriceTV);
//        product_descriptionTV = view.findViewById(R.id.product_descriptionTV);
//        addressTV = view.findViewById(R.id.addressTV);
//        cityTV = view.findViewById(R.id.cityTV);
//        StateTV = view.findViewById(R.id.StateTV);
//        CountryTV = view.findViewById(R.id.CountryTV);
//        PhoneTV = view.findViewById(R.id.PhoneTV);






        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            ItemUid = bundle.getString("ItemUid");
        }



        return view;
    }
}