package com.example.flashcart.SellerPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.flashcart.Adaptor.AdaptorPromotionCodeShop;
import com.example.flashcart.Model.ModelPromotionCode;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SellerPromotionCodeFragment extends Fragment {

    EditText search;
    RecyclerView recyclerView;
    ImageView addpromotioncodebtn;

    FirebaseAuth firebaseAuth;

    private ArrayList<ModelPromotionCode> promotionCodeArrayList;
    private AdaptorPromotionCodeShop adaptorPromotionCodeShop;

    public SellerPromotionCodeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_promotion_code, container, false);

        addpromotioncodebtn = view.findViewById(R.id.addpromotioncodebtn);
        recyclerView = view.findViewById(R.id.promoRv);

        firebaseAuth = FirebaseAuth.getInstance();


        loadAllPromoCodes();


        addpromotioncodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigatetoADDpromotionFragmnet();

            }
        });



        return view;
    }

    private void navigatetoADDpromotionFragmnet() {

        Seller_Add_PromotionCode_fragmnet newFragment = new Seller_Add_PromotionCode_fragmnet();


        // Get the fragment manager
        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        // Begin a new transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the previous fragment with the new fragment
        fragmentTransaction.replace(R.id.Frame_layout, newFragment);

        // Add the transaction to the back stack so the user can navigate back to the previous fragment
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();


    }


    private void loadAllPromoCodes(){

        promotionCodeArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(firebaseAuth.getUid()).child("PromotionCodes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        promotionCodeArrayList.clear();

                        for (DataSnapshot ds : snapshot.getChildren()){

                            ModelPromotionCode modelPromotionCode = ds.getValue(ModelPromotionCode.class);
                            promotionCodeArrayList.add(modelPromotionCode);

                            Log.d("Code",modelPromotionCode.getPromoCode());

                        }

                        adaptorPromotionCodeShop = new AdaptorPromotionCodeShop(getContext(),promotionCodeArrayList);
                        recyclerView.setAdapter(adaptorPromotionCodeShop);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

}