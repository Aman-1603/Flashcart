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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.Adaptor.AdaptorCartItem;
import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class UserAddToCartFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;


    TextView producttotal,deliveryFeeTv,subtotalTv;

    Button finalorder;

    String shopUid,deliveryfee;

    String myLatiitude,myLongitude,myphone;

    double allTotalPrice = 0.0;
    double subtotalPrice = 0.0;


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


        deliveryFeeTv = view.findViewById(R.id.deliveryfeeTv);
        subtotalTv = view.findViewById(R.id.subtotalTv);
        producttotal = view.findViewById(R.id.productTotalTv);
        finalorder = view.findViewById(R.id.finalorderTv);

        firebaseAuth = FirebaseAuth.getInstance();




        loadmyInfo();
        loadcartItem();


        finalorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myLatiitude.equals("") || myLatiitude.equals("null") || myLongitude.equals("") || myLongitude.equals("null")){

                    Toast.makeText(getContext(), "Please Enter your Location detail in your profile page", Toast.LENGTH_SHORT).show();

                    return;

                }

                if (myphone.equals("") || myphone.equals("null")){

                    Toast.makeText(getContext(), "Please Enter your Phone detail in your profile page", Toast.LENGTH_SHORT).show();

                    return;

                }

                if (list.size() == 0){

                    Toast.makeText(getContext(), "No item in Cart", Toast.LENGTH_SHORT).show();
                    return;

                }

                submitOrder();


            }
        });


        return view;
    }

    private void submitOrder() {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait while we are placing you order at our end");
        progressDialog.show();



        String timestamp = ""+System.currentTimeMillis();

        String cost = subtotalTv.toString().trim().replace("₹","");

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("orderId",""+timestamp);
        hashMap.put("orderTime",""+timestamp);
        hashMap.put("orderStatus","In Progress");
        hashMap.put("orderBy",""+firebaseAuth.getUid());
        hashMap.put("orderTo",""+shopUid);
        hashMap.put("orderFinalSubTotal",""+subtotalPrice);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(shopUid).child("Orders");
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //order information is added now add order item to database

                        for (int i=0; i<list.size(); i++){

                            String pId = list.get(i).getItemUid();
                            String id = list.get(i).getUid();
                            String cost = list.get(i).getPrice();
                            String name = list.get(i).getTitle();
                            String price = list.get(i).getPriceEach();
                            String quantity = list.get(i).getQuantity();
                            String ProductIcon = list.get(i).getProductIcon();

                            HashMap<String,String> hashMap1 = new HashMap<>();
                            hashMap1.put("pId",pId);
                            hashMap1.put("name",name);
                            hashMap1.put("cost",cost);
                            hashMap1.put("price",price);
                            hashMap1.put("quantity",quantity);
                            hashMap1.put("ProductIcon",ProductIcon);

                            ref.child(timestamp).child("Items").child(pId).setValue(hashMap1);



                        }


//                        Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                    }
                });




    }


    private void loadmyInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot ds : datasnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            myphone = ""+ds.child("Phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();
                            String state = ""+ds.child("state").getValue();
                            String country = ""+ds.child("country").getValue();
                            String address = ""+ds.child("address").getValue();

                            myLatiitude= ""+ds.child("latitude").getValue();
                            myLongitude  = ""+ds.child("longitude").getValue();



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

                            shopUid = modelcartrecive.getShopUid();

                           allTotalPrice = allTotalPrice + Double.parseDouble(modelcartrecive.getPrice());

                            list.add(modelcartrecive);
                        }

                        adaptorCartItem.notifyDataSetChanged();



                        producttotal.setText("₹"+allTotalPrice);

                        Log.d("FirebaseData", "Data retrieved: " + datasnapshot.getValue());
                        Log.d("Adapter", "Adapter set");
                        Log.d("shopuid",shopUid);


                        loadDeliveryFee(shopUid);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadDeliveryFee(String shopUid) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        deliveryfee = ""+datasnapshot.child("deliveryFees").getValue();

                        deliveryFeeTv.setText("₹"+deliveryfee);

                        subtotalPrice = allTotalPrice + Double.parseDouble(deliveryfee);

                        subtotalTv.setText("₹"+subtotalPrice);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}