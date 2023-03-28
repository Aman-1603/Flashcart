package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.flashcart.Adaptor.AdaptorOrderUser;
import com.example.flashcart.Model.ModelOrderUser;
import com.example.flashcart.Model.ModelShop;
import com.example.flashcart.R;
import com.example.flashcart.User_review_write_fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserOrderPage extends Fragment {


    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    private ArrayList<ModelOrderUser> orderList;
    AdaptorOrderUser adaptorOrderUser;

    Button review;

    


    public UserOrderPage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_order_page, container, false);


        recyclerView = view.findViewById(R.id.recyclarItemAddToCart);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        review = view.findViewById(R.id.reviewbutton);


        firebaseAuth = FirebaseAuth.getInstance();




        loadorders();



        return view;
    }

    private void loadorders() {
        // Initialize orderList
        orderList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // Clear the orderList
                orderList.clear();

                for (DataSnapshot ds : datasnapshot.getChildren()){
                    String uid = ""+ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Orders");
                    ref.orderByChild("orderBy").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                    if (datasnapshot.exists()){

                                        for (DataSnapshot ds: datasnapshot.getChildren()){

                                            ModelOrderUser modelOrderUser = ds.getValue(ModelOrderUser.class);



                                            orderList.add(modelOrderUser);
                                            Log.d("data",""+ds);
                                        }

                                        // Create a new adapter with the updated orderList
                                        adaptorOrderUser = new AdaptorOrderUser(getContext(),orderList);
                                        // Set the adapter to the RecyclerView
                                        recyclerView.setAdapter(adaptorOrderUser);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Sorry to fetch the order list", Toast.LENGTH_SHORT).show();
            }
        });
    }



}