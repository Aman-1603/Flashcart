package com.example.flashcart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.Adaptor.AdaptorOrderDetailUser;
import com.example.flashcart.Model.ModelCartItemRecieve;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class SellerOrderDetailFragmnet extends Fragment {


    TextView orderdetailId,orderdetaildate,orderdetailstatus,orderdetailshopname,orderdetailphone,orderdetailaddress,subtotalTv,totalitemTv;

    String OrderId,OrderBy;

    FirebaseAuth firebaseAuth;

    String sourceLatitude,sourceLongitude,destinationLatitude,destinationLongitude;

    FloatingActionButton goMap,EditOrderStatus;

    RecyclerView recycler1;


    ArrayList<ModelCartItemRecieve> orderUserArrayList;
    AdaptorOrderDetailUser adaptorCartItem;

    public SellerOrderDetailFragmnet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_order_detail_fragmnet, container, false);

        firebaseAuth = FirebaseAuth.getInstance();


        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            OrderId = bundle.getString("orderId");
            OrderBy = bundle.getString("orderBy");
        }


        orderdetailId = view.findViewById(R.id.orderdetailId);
        orderdetaildate = view.findViewById(R.id.orderdetaildate);
        orderdetailphone = view.findViewById(R.id.orderdetailphone);
        orderdetailstatus = view.findViewById(R.id.orderdetailstatus);
        orderdetailshopname = view.findViewById(R.id.orderdetailshopname);
        orderdetailaddress = view.findViewById(R.id.orderdetailaddress);
        subtotalTv = view.findViewById(R.id.subtotalTv);
        totalitemTv = view.findViewById(R.id.totalitemTv);
        goMap = view.findViewById(R.id.goMap);
        EditOrderStatus = view.findViewById(R.id.EditOrderStatus);

        recycler1 = view.findViewById(R.id.recycler1);

        loadMyInfo();
        loadBuyerInfo();
        loadOredrDetails();
        loadOrderItem();




        goMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });


        EditOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                editOrderStatusDialog();
                
            }
        });



        return view;
    }

    private void editOrderStatusDialog() {

        String[] option = {"In Progress","Order Packed","Order Shipped","Order Arrived to Your Near location","Order out for delivery","Complete","Cancelled"};

        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Edit Order Status")
                .setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        String selectedOption = option[i];

                        editOrderStatus(selectedOption);

                    }
                }).show();

    }

    private void editOrderStatus(String selectedOption) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus", ""+selectedOption);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Orders").child(OrderId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getContext(), "Status Updated Successfully", Toast.LENGTH_SHORT).show();

                        loadOredrDetails();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void loadOrderItem() {

        orderUserArrayList = new ArrayList<>();
        adaptorCartItem = new AdaptorOrderDetailUser(getContext(),orderUserArrayList);
        recycler1.setAdapter(adaptorCartItem);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(firebaseAuth.getUid()).child("Orders").child(OrderId).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        orderUserArrayList.clear();

                        for (DataSnapshot ds : datasnapshot.getChildren()){

                            ModelCartItemRecieve modelOrderUser = ds.getValue(ModelCartItemRecieve.class);

                            orderUserArrayList.add(modelOrderUser);

                        }

                        //all item added to list now set adaptor

                        adaptorCartItem = new AdaptorOrderDetailUser(getContext(),orderUserArrayList);

                        recycler1.setAdapter(adaptorCartItem);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void loadOredrDetails() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(firebaseAuth.getUid()).child("Orders").child(OrderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {




                        String orderBy = ""+datasnapshot.child("orderBy").getValue();

                        String orderCost = ""+datasnapshot.child("orderFinalSubTotal").getValue();
                        String orderld = ""+datasnapshot.child("orderId").getValue();

                        String orderStatus = ""+datasnapshot.child("orderStatus").getValue();

                        String orderTime = ""+datasnapshot.child("orderTime").getValue();
                        String orderTo = ""+datasnapshot.child("orderTo").getValue();

                        String deliveryFee = ""+datasnapshot.child("deliveryFee").getValue();
                        String latitude = ""+datasnapshot.child("latitude").getValue();
                        String longitude = ""+datasnapshot.child("longitude").getValue();



                        //now convert time to porper time stamp

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(orderTime));
                        String dateformate = DateFormat.format("dd/MM/yyyy",calendar).toString();




                        if (orderStatus.equals("In Progress")){
                            orderdetailstatus.setTextColor(getResources().getColor(R.color.purple_200));


                        } else if (orderStatus.equals("Complete")) {
                            orderdetailstatus.setTextColor(getResources().getColor(R.color.colorGreen1));

                        } else if (orderStatus.equals("Cancelled")) {

                            orderdetailstatus.setTextColor(getResources().getColor(R.color.red));


                        }
                        else if (orderStatus.equals("Order Packed")) {

                            orderdetailstatus.setTextColor(getResources().getColor(R.color.purple_200));


                        }
                        else if (orderStatus.equals("Order Shipped")) {

                            orderdetailstatus.setTextColor(getResources().getColor(R.color.purple_500));


                        }

                        else if (orderStatus.equals("Order Arrived to Your Near location")) {

                            orderdetailstatus.setTextColor(getResources().getColor(R.color.purple_700));


                        }
                        else if (orderStatus.equals("Order out for delivery")) {

                            orderdetailstatus.setTextColor(getResources().getColor(R.color.purple_700));


                        }



                        orderdetailId.setText(OrderId);
                        orderdetailstatus.setText(orderStatus);
                        subtotalTv.setText(orderCost);
                        orderdetaildate.setText(dateformate);





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void openMap() {

        try {
            String address = "https://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
            startActivity(intent);



        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private void loadMyInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        sourceLatitude = ""+datasnapshot.child("latitude").getValue();
                        sourceLongitude = ""+datasnapshot.child("longitude").getValue();




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void loadBuyerInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(OrderBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        destinationLatitude = ""+datasnapshot.child("latitude").getValue();
                        destinationLongitude = ""+datasnapshot.child("longitude").getValue();


                        String email = ""+datasnapshot.child("email").getValue();
                        String phone = ""+datasnapshot.child("Phone").getValue();
                        String address = ""+datasnapshot.child("address").getValue();

                        orderdetailshopname.setText(email);
                        orderdetailphone.setText(phone);
                        orderdetailaddress.setText(address);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}