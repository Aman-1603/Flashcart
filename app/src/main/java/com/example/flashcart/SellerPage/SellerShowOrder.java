package com.example.flashcart.SellerPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

    TextView textshowstatus;

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
        textshowstatus = view.findViewById(R.id.textshowstatus);

        OrderRvSeller = view.findViewById(R.id.OrderRvSeller);

        firebaseAuth = FirebaseAuth.getInstance();

        loadAllOrders();



        filterproductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] options = {"All", "In Progress", "Complete", "Cancelled"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Filters Orders")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0){

                                    textshowstatus.setText("Showing All Products");
                                    adaptorOrderShop.getFilter().filter("");  //we pass nothing showing all products



                                }else {

                                    String optionClicked = options[which];
                                    textshowstatus.setText("Showing " + optionClicked + " orders");
                                    adaptorOrderShop.getFilter().filter(optionClicked);

                                }

                            }
                        })
                        .show();

            }
        });

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