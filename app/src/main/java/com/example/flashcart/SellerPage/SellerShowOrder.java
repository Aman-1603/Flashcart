package com.example.flashcart.SellerPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.flashcart.Adaptor.AdaptorOrderShop;
import com.example.flashcart.Model.ModelOrderShop;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SellerShowOrder extends Fragment {


    EditText searchproductEt1;
    ImageButton filterproductBtn;
    RecyclerView OrderRvSeller;

    FirebaseAuth firebaseAuth;

    ArrayList<ModelOrderShop> orderShopArrayList;
    AdaptorOrderShop adaptorOrderShop;


    public SellerShowOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_show_order, container, false);

        searchproductEt1 = view.findViewById(R.id.searchproductEt1);
        filterproductBtn = view.findViewById(R.id.filterproductBtn);

        OrderRvSeller = view.findViewById(R.id.OrderRvSeller);

        firebaseAuth = FirebaseAuth.getInstance();

        loadAllOrders();




        return  view;
    }

    private void loadAllOrders() {

        orderShopArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(firebaseAuth.getUid()).child("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        orderShopArrayList.clear();

                        for (DataSnapshot ds : datasnapshot.getChildren()){

                            ModelOrderShop modelOrderShop = ds.getValue(ModelOrderShop.class);

                            orderShopArrayList.add(modelOrderShop);


                        }

                        adaptorOrderShop = new AdaptorOrderShop(getContext(),orderShopArrayList);

                        OrderRvSeller.setAdapter(adaptorOrderShop);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}