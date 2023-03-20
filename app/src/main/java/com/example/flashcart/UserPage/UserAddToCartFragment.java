package com.example.flashcart.UserPage;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flashcart.Adaptor.AdaptorCartItem;
import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserAddToCartFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    AdaptorCartItem adaptorCartItem;
    ArrayList<ModelCartItemRecieve> list;


    public UserAddToCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_add_to_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclarItemAddToCart);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseAuth = FirebaseAuth.getInstance();

        loadcartItem();

        return view;
    }

    private void loadcartItem() {


        list = new ArrayList<>();
        adaptorCartItem = new AdaptorCartItem(getContext(),list);
        recyclerView.setAdapter(adaptorCartItem);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("AddtoCart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {



                        for (DataSnapshot ds : datasnapshot.getChildren()){
                            ModelCartItemRecieve modelcartrecive = ds.getValue(ModelCartItemRecieve.class);
                            list.add(modelcartrecive);
                        }

                        adaptorCartItem.notifyDataSetChanged();

                        //now setup Adaptor



                        Log.d("FirebaseData", "Data retrieved: " + datasnapshot.getValue());
                        Log.d("Adapter", "Adapter set");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}