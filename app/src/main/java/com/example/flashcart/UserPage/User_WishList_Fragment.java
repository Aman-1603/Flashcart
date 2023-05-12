package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.flashcart.Adaptor.AdaptorAccountWishList;
import com.example.flashcart.Adaptor.AdaptorWishList;
import com.example.flashcart.Model.ModelWishList;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class User_WishList_Fragment extends Fragment {


    RecyclerView wishListRV;

    FirebaseAuth firebaseAuth;


    AdaptorAccountWishList adaptorWishList;
    ArrayList<ModelWishList> list;

    public User_WishList_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user__wish_list_, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        wishListRV = view.findViewById(R.id.wishListRV);


        loadWishList();


        return view;
    }

    private void loadWishList() {

        list = new ArrayList<>();
        adaptorWishList = new AdaptorAccountWishList(getContext(),list);
        wishListRV.setAdapter(adaptorWishList);


        try {


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("UserWishList")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {


                            for (DataSnapshot ds : datasnapshot.getChildren()) {
                                ModelWishList modelWishList = ds.getValue(ModelWishList.class);


                                Log.d("one",modelWishList.getTitle());
                                Log.d("two",modelWishList.getPriceEach());
                                Log.d("three",modelWishList.getProductIcon());
                                Log.d("data from model", String.valueOf(modelWishList));

                                list.add(modelWishList);
                            }

                            adaptorWishList.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }catch (Exception e){

            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }




    }
}