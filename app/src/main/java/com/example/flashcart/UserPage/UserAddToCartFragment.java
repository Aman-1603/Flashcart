package com.example.flashcart.UserPage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashcart.Adaptor.AdaptorCartItem;
import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.R;
import com.example.flashcart.categorylist.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;





public class UserAddToCartFragment extends Fragment implements PaymentResultListener {

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;


    TextView producttotal,deliveryFeeTv,subtotalTv,promotiondescriptionTv,discountTv;

    Button finalorder;

    String shopUid,deliveryfee;
    double finalpromoprice = 0;

    String myLatiitude,myLongitude,myphone;

    double allTotalPrice = 0.0;
    double subtotalPrice = 0.0;

    EditText promocodeEt;

    FloatingActionButton validateBtn;

    Button applyBtn;

    int ispromoCheck = 0;


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
        promotiondescriptionTv = view.findViewById(R.id.promotionDescription);
        promocodeEt = view.findViewById(R.id.promocodeEt);
        discountTv = view.findViewById(R.id.discount);
        validateBtn = view.findViewById(R.id.promoValidatebtn);
        applyBtn = view.findViewById(R.id.applypromocodebtn);

        firebaseAuth = FirebaseAuth.getInstance();




        loadmyInfo();
        loadcartItem();




        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String promocode = promocodeEt.getText().toString().trim();

                checkPromoCodeAvailability(promocode);
            }
        });



        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ispromoCheck == 1){

                    priceWithDiscount();

                    finalpromoprice = Double.parseDouble(promoprice);


                }else {

                    Toast.makeText(getContext(), "Please First Apply promocode then Apply...", Toast.LENGTH_SHORT).show();

                }

            }
        });



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


                PaymentNow("1");

                submitOrder();




            }
        });


        return view;
    }

    private void PaymentNow(String subtotalPrice) {

        final Activity activity = getActivity();

        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_test_Xhoi5wf0u1ChSi");
        checkout.setImage(R.drawable.ic_launcher_background);

        double finalamount = Float.parseFloat(subtotalPrice)*100;


        try {

            JSONObject option = new JSONObject();
            option.put("Name","Aman");
            option.put("description","orderId");
            option.put("Image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            option.put("theme.color","#3399cc");
            option.put("currency","INR");
            option.put("Final Amount",finalamount);
            option.put("Name","ansariaman1603@gmail.com");
            option.put("Contact","9265413820");


            checkout.open(getActivity(),option);

            showsucesspage();



        }catch (Exception e){

            Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            showsucesspage();

        }

    }


    private void priceWithDiscount(){


        discountTv.setText("₹" + promoprice);
        deliveryFeeTv.setText("₹"+ deliveryfee);
        producttotal.setText("₹"+allTotalPrice);
        subtotalTv.setText("₹"+(allTotalPrice + Double.parseDouble(deliveryfee.replace("₹","")) - Double.parseDouble(promoprice)));


    }


    private void priceWithoutDiscount(){

        discountTv.setText("₹0");
        deliveryFeeTv.setText("₹"+deliveryfee);
        producttotal.setText("₹"+allTotalPrice);
        subtotalTv.setText("₹"+(allTotalPrice + Double.parseDouble(deliveryfee.replace("₹",""))));


    }

    public boolean isPromoCodeApplied = false;

    public String promoId,promotimestamp,promoCode,promoDescritpion,promoExpdate,promoMinimumOrder,promoprice;

    public void checkPromoCodeAvailability(String PromotioCode){

        //setup progress bar

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Checking Promo Code Availability at our end please wait ......");
        progressDialog.setCanceledOnTouchOutside(false);


        //promocode is not applied yet...
        isPromoCodeApplied = false;
        applyBtn.setText("Apply");

        priceWithoutDiscount();


        //check promocode availability

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(shopUid).child("PromotionCodes").orderByChild("PromoCode").equalTo(PromotioCode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //checking if promo code exist

                        if (snapshot.exists()){

                            progressDialog.dismiss();
                            for (DataSnapshot ds : snapshot.getChildren()){

                                promoId = ""+ds.child("id").getValue();
                                promotimestamp = ""+ds.child("timestamp").getValue();
                                promoCode = ""+ds.child("PromoCode").getValue();
                                promoDescritpion = ""+ds.child("PromoDescription").getValue();
                                promoExpdate = ""+ds.child("ExpireDate").getValue();
                                promoMinimumOrder = ""+ds.child("MinOrderPrice").getValue();
                                promoprice = ""+ds.child("PromoPrice").getValue();


                                //now we will check if code id expire or not


                                checkCodeExpireDate();

                            }

                        }else {

                            //promo code not exist

                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Entered Promo Code not Exist", Toast.LENGTH_SHORT).show();
                            applyBtn.setVisibility(View.GONE);
                            promotiondescriptionTv.setVisibility(View.GONE);

                            promotiondescriptionTv.setText("");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void checkCodeExpireDate(){


        //check current date

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;  // because month start from 0 instead of 1;

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //now we will concatinate date in real form like 18/04/2023

        String todayDate =  day + "/"+ month + "/" + year;

        //check expiry


        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            //current date

            Date CurrentDate = simpleDateFormat.parse(todayDate);
            Date ExpireDate = simpleDateFormat.parse(promoExpdate);

            Log.d("current date", String.valueOf(CurrentDate));

            if (ExpireDate.compareTo(CurrentDate) > 0){

                //so 1 occure after 2  that means (date is not expired)

                checkMinimumOrderPrice();

            } else if (ExpireDate.compareTo(CurrentDate) < 0) {

                //date 1 occured before date 2 (Code expired)

                Toast.makeText(getContext(), "Promotion Code Expired on "+ promoExpdate, Toast.LENGTH_SHORT).show();

                applyBtn.setVisibility(View.GONE);
                promotiondescriptionTv.setVisibility(View.GONE);
                promotiondescriptionTv.setText("");



            } else if (ExpireDate.compareTo(CurrentDate) == 0) {

                //both dates are equal

                checkMinimumOrderPrice();

            }

        }catch (Exception e){

            Log.d("checkCodeexpiredate","checkCodeexpiredate");

            Log.d("checkCodeexpiredate",e.getLocalizedMessage());

            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    private void checkMinimumOrderPrice(){


        Log.d("checkMinimumOrderPrice","checkMinimumOrderPrice");

        if (Double.parseDouble(String.format("%2f",subtotalPrice))<Double.parseDouble(promoMinimumOrder)){

            Toast.makeText(getContext(), "This order valid within minimum order of "+promoMinimumOrder, Toast.LENGTH_SHORT).show();
            applyBtn.setVisibility(View.GONE);
            promotiondescriptionTv.setVisibility(View.GONE);
            promotiondescriptionTv.setText("");

        }else {


            applyBtn.setVisibility(View.VISIBLE);
            promotiondescriptionTv.setVisibility(View.VISIBLE);
            promotiondescriptionTv.setText(""+promoDescritpion);


            promotiondescriptionTv.setText(promoDescritpion);

//            priceWithDiscount();

            //if all condition is perfect then call priceWithDiscount(); to change price

            ispromoCheck = 1;


        }


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
        hashMap.put("orderFinalSubTotal",""+(subtotalPrice - finalpromoprice));//if promo price applicable then deduct othervise bydefualb be 0




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


                        Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        prepareNotificationMessage(timestamp);

                        //now next code which will show order success page will go in

//                        public void onResponse(JSONObject response)

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

//                        Log.d("FirebaseData", "Data retrieved: " + datasnapshot.getValue());
//                        Log.d("Adapter", "Adapter set");
//                        Log.d("shopuid",shopUid);


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


    private void prepareNotificationMessage(String orderId){

        //when user Place order,send notification to user

        //prepare data for notifications
        String NOTIFICATION_TOPIC = "/topics" + Constants.FCM_TOPIC; //must be same as subscriber by user
        String NOTIFICATION_TITLE = "New Order " + orderId;
        String NOTIFICATION_MESSAGE = "Congratulations you have new order !....";
        String NOTIFICATION_TYPE = "NewOrder";

        //now perpare jason (What to send and where to send)

        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();

        try {
            //what to send
            notificationBodyJo.put("notificationType",NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid",firebaseAuth.getUid());
            notificationBodyJo.put("sellerUid",shopUid);
            notificationBodyJo.put("orderId",orderId);
            notificationBodyJo.put("notificationTitle",NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage",NOTIFICATION_MESSAGE);

            //where to send

            notificationJo.put("to",NOTIFICATION_TOPIC); // to all who subscribed to these topic
            notificationJo.put("data",notificationBodyJo);



        }catch (Exception e){

            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        sendFcmNotification(notificationJo,orderId);

    }

    private void sendFcmNotification(JSONObject notificationJo, String orderId) {

        //send volley request

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //after sending fcm startorderactivity

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //if failed sending fcm, still start  order activity


            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                //put required headers

                Map<String,String> header = new HashMap<>();
                header.put("Content_Type", "application/jason");
                header.put("Authorization","key" + Constants.FCM_KEY);


                return header;

            }
        };


        //enque the volly request

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);



    }


    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(getContext(), "Payment success", Toast.LENGTH_SHORT).show();

        showsucesspage();


    }


    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getContext(), "Payment failed", Toast.LENGTH_SHORT).show();
        showsucesspage();

    }


    private void showsucesspage() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserFinalOrder_Success_Fail_messageshow newFragment = new UserFinalOrder_Success_Fail_messageshow();

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
        }, 3000); // 3 seconds delay


    }

}