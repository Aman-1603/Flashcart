package com.example.flashcart.UserPage;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.flashcart.Adaptor.AdaptorShopUser;
import com.example.flashcart.Model.ModelShop;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserShowShop extends Fragment {



     RecyclerView recyclerView;
     FirebaseAuth firebaseAuth;
     ProgressDialog progressDialog;

     ArrayList<ModelShop> shopsList;
     AdaptorShopUser adaptorShopUser;

    public UserShowShop() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_show_shop, container, false);

        recyclerView = view.findViewById(R.id.shoprecyclerView);


        firebaseAuth = FirebaseAuth.getInstance();

        loaduserProfile();


        return view;
    }

    private void loaduserProfile() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for (DataSnapshot ds: datasnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();

                            //now we will load the shops

                            loadShop(city);



                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadShop(String mycity) {

        //initializing li
        shopsList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                         shopsList.clear();
                         for (DataSnapshot ds: datasnapshot.getChildren()){
                             ModelShop modelShop = ds.getValue(ModelShop.class);

                             String shopcity = ""+ds.child("city").getValue();

                             //show only city shop



                             if(shopcity.equals(mycity)){  //if want to display allshop then we will skip it "if statement"
                                 shopsList.add(modelShop);
                             }
                         }

                         adaptorShopUser = new AdaptorShopUser(getContext(),shopsList);

                         //set adaptor

                        recyclerView.setAdapter(adaptorShopUser);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


}