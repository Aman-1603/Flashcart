package com.example.flashcart.SellerPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.LoginActivity;
import com.example.flashcart.MainsellerActivity;
import com.example.flashcart.R;
import com.example.flashcart.categorylist.Constants;
import com.example.flashcart.profilePage.ProfileActivitySeller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerHomePage1_fragment extends Fragment {


    private TextView nameTV,shopnameTV,emailTV,filterproductTv;
    ImageButton logoutbtn,profile,filterproductBtn;

     EditText searchproductEt;
    private RecyclerView productRv;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelProduct> productList;
    private AdaptorProductSeller adaptorProductSeller;

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
        searchproductEt = view.findViewById(R.id.searchproductEt);
        filterproductBtn = view.findViewById(R.id.filterproductBtn);
        filterproductTv = view.findViewById(R.id.flterproductTv);
        productRv = view.findViewById(R.id.recyclarItem);




        firebaseAuth = FirebaseAuth.getInstance();
        CheckUser();

        //show product to listview
        loadallProducts();


        searchproductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    adaptorProductSeller.getFilter().filter(s);
                }catch (Exception e){
                  e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CheckUser();
                startActivity(new Intent(getContext(), ProfileActivitySeller.class));
            }
        });


        filterproductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Categories:")
                        .setItems(Constants.productcategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected items
                                String selected = Constants.productcategories1[which];
                                filterproductTv.setText(selected);

                                if(selected.equals("All")){
                                    //load all

                                    loadallProducts();

                                }else{
                                    loadFileterdProducts(selected);
                                }

                            }
                        }).show();
            }
        });

        return view;
    }

    private void loadFileterdProducts(String selected) {

        productList = new ArrayList<>();

        //get all products

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        //before getting data first reset List
                        productList.clear();

                        for (DataSnapshot ds : datasnapshot.getChildren()) {

                            String productCatergory = ""+ds.child("ProductCategory").getValue();

                            //if selected category match product then add to list

                            if(selected.equals(productCatergory)){
                                ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                                productList.add(modelProduct);
                            }



                        }

                        //set Adaptor

                        adaptorProductSeller = new AdaptorProductSeller(getContext(), productList);

                        //set data to adaptor

                        productRv.setAdapter(adaptorProductSeller);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadallProducts() {

        productList = new ArrayList<>();

        //get all products

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        //before getting data first reset List
                        productList.clear();

                        for(DataSnapshot ds: datasnapshot.getChildren()){
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }

                        //set Adaptor

                        Toast.makeText(getContext(), "uptothese its working", Toast.LENGTH_SHORT).show();
                        adaptorProductSeller = new AdaptorProductSeller(getContext(),productList);

                        //set data to adaptor

                        productRv.setAdapter(adaptorProductSeller);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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