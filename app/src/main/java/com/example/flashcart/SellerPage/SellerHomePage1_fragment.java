package com.example.flashcart.SellerPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcart.LoginActivity;
import com.example.flashcart.MainsellerActivity;
import com.example.flashcart.R;
import com.example.flashcart.profilePage.ProfileActivitySeller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerHomePage1_fragment extends Fragment {


    private TextView nameTV,shopnameTV,emailTV;
    ImageButton logoutbtn,profile;

    private FirebaseAuth firebaseAuth;

    public SellerHomePage1_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home_page1_fragment, container, false);

        nameTV = view.findViewById(R.id.nametv);
        shopnameTV = view.findViewById(R.id.shopanmetv);
        emailTV = view.findViewById(R.id.emailtv);
        logoutbtn = view.findViewById(R.id.logoutbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        CheckUser();

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CheckUser();
                startActivity(new Intent(getContext(), ProfileActivitySeller.class));
            }
        });



        return view;
    }


    private void CheckUser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
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