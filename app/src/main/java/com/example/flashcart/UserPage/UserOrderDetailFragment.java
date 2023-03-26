package com.example.flashcart.UserPage;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.flashcart.Adaptor.AdaptorOrderDetailUser;
import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class UserOrderDetailFragment extends Fragment {


    String orderTo,orderId;

    TextView orderdetailId,orderdetaildate,orderdetailstatus,orderdetailshopname,orderdetailitem,orderdetailfinalamount,orderdetailaddress;
    RecyclerView recycler1;

    TextView orderdate,orderdeliverTv;

    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    ArrayList<ModelCartItemRecieve> orderUserArrayList;
    AdaptorOrderDetailUser adaptorCartItem;



    public UserOrderDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_order_detail, container, false);


        orderdetailId = view.findViewById(R.id.orderdetailId);
        orderdetaildate = view.findViewById(R.id.orderdetaildate);
        orderdetailstatus = view.findViewById(R.id.orderdetailstatus);
        orderdetailshopname = view.findViewById(R.id.orderdetailshopname);
        orderdetailitem = view.findViewById(R.id.orderdetailitem);
        orderdetailfinalamount = view.findViewById(R.id.orderdetailfinalamount);
        orderdetailaddress = view.findViewById(R.id.orderdetailaddress);
        orderdate  = view.findViewById(R.id.text9);
        progressBar = view.findViewById(R.id.progress);
        orderdeliverTv = view.findViewById(R.id.orderdeliverTv);


        recycler1 = view.findViewById(R.id.recycler1);
        recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));



        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            orderTo = bundle.getString("orderTo");
            orderId = bundle.getString("orderId");
        }


        loadshopInfo();
        loadorderDetails();
        loadorderItems();


        return view;
    }

    private void loadorderItems() {

        orderUserArrayList = new ArrayList<>();
        adaptorCartItem = new AdaptorOrderDetailUser(getContext(),orderUserArrayList);
        recycler1.setAdapter(adaptorCartItem);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(orderTo).child("Orders").child(orderId).child("Items")
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

    private void loadorderDetails() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(orderTo).child("Orders").child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        String orderBy=""+datasnapshot.child("orderBy").getValue();

                        String orderCost = ""+datasnapshot.child("orderFinalSubTotal").getValue();

                        String orderId = ""+datasnapshot.child("orderId").getValue();

                        String orderStatus = ""+datasnapshot.child("orderStatus").getValue();

                        String orderTime=""+datasnapshot.child("orderTime").getValue();

                        String orderTo=""+datasnapshot.child("orderTo").getValue();

                        String deliveryFee=""+datasnapshot.child("deliveryFee").getValue();


                        //convert timestamp to proper format

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(orderTime));

                        String formatdate = DateFormat.format("dd/MM/yyyy hh:mm a",calendar).toString();


                        if (orderStatus.equals("In Progress")){
                            orderdetailstatus.setTextColor(getResources().getColor(R.color.purple_200));
                            progressBar.setProgress(50);


                        } else if (orderStatus.equals("Complete")) {
                            orderdetailstatus.setTextColor(getResources().getColor(R.color.colorGreen1));
                            progressBar.setProgress(100);

                        } else if (orderStatus.equals("Cancelled")) {

                            orderdetailstatus.setTextColor(getResources().getColor(R.color.red));
                            progressBar.setProgress(100);
                            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                            orderdeliverTv.setText("Order Cancelled");
                            orderdeliverTv.setTextColor(getResources().getColor(R.color.red));


                        }


                        orderdetailId.setText(orderId);
                        orderdetailstatus.setText(orderStatus);
                        orderdetailfinalamount.setText(orderCost);
                        orderdetaildate.setText(formatdate);
                        orderdate.setText(formatdate);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadshopInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(orderTo)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        String shopName = ""+datasnapshot.child("shopName").getValue();
                        orderdetailshopname.setText(shopName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}