package com.example.flashcart.SellerPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashcart.Adaptor.AdaptorOrderDetailUser;
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
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SellerOrderDetailFragmnet extends Fragment {


    TextView orderdetailId,orderdetaildate,orderdetailstatus,orderdetailshopname,orderdetailphone,orderdetailaddress,subtotalTv,totalitemTv;

    String OrderId,OrderBy;

    FirebaseAuth firebaseAuth;

    String sourceLatitude,sourceLongitude,destinationLatitude,destinationLongitude;

    FloatingActionButton goMap,EditOrderStatus,generatepdf;

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
        generatepdf = view.findViewById(R.id.generatepdf);

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

        generatepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatepdf();
            }
        });



        return view;
    }

    private void generatepdf() {

        try {
            String fileName = "order_detail.pdf";
            String filePath = getContext().getExternalFilesDir(null) + "/" + fileName;
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);





            Document document = new Document(pdf);

            // Retrieve store name from where it is stored in your app
            // Add store name to the PDF document
            Paragraph storeNameParagraph = new Paragraph("FlashCart");
            storeNameParagraph.setTextAlignment(TextAlignment.CENTER);
            storeNameParagraph.setFontSize(24);
            storeNameParagraph.setFontColor(ColorConstants.BLUE);
            document.add(storeNameParagraph);





            // Add order details to the PDF document
            document.add(new Paragraph("Order Details"));
            document.add(new Paragraph("Order ID: " + OrderId));
            document.add(new Paragraph("Order By: " + OrderBy));
            document.add(new Paragraph("Order Date: " + orderdetaildate.getText().toString()));
            document.add(new Paragraph("Order Status: " + orderdetailstatus.getText().toString()));
            document.add(new Paragraph("Shop Name: " + orderdetailshopname.getText().toString()));
            document.add(new Paragraph("Phone: " + orderdetailphone.getText().toString()));
            document.add(new Paragraph("Address: " + orderdetailaddress.getText().toString()));
            document.add(new Paragraph("Subtotal: " + subtotalTv.getText().toString()));
            document.add(new Paragraph("Total Items: " + totalitemTv.getText().toString()));

            // Add order items to the PDF document
            document.add(new Paragraph("Order Items"));
            for (ModelCartItemRecieve orderItem : orderUserArrayList) {
                document.add(new Paragraph(orderItem.getItemUid() + " - " + orderItem.getPrice() + " - " + orderItem.getQuantity()));
            }

            document.close();

            Toast.makeText(getContext(), "PDF Generated Successfully", Toast.LENGTH_SHORT).show();

            // Open the PDF file
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            startActivity(intent);



//            sendPDFviaSMS(orderdetailphone,filePath);


            // Open the PDF file
            Uri pdfUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error Generating PDF", Toast.LENGTH_SHORT).show();
        }

    }

//    private void sendPDFviaSMS(TextView orderdetailphone, String filePath) {
//
//        try {
//            File file = new File(filePath);
//            Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
//
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.putExtra("address", orderdetailphone.toString());
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//            intent.setType("application/pdf");
//            intent.setPackage("com.android.mms");
//            startActivity(intent);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "Error sending PDF", Toast.LENGTH_SHORT).show();
//        }
//
//    }

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

                    String message = "Order is Now"+selectedOption;

                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getContext(), "Status Updated Successfully", Toast.LENGTH_SHORT).show();

                        loadOredrDetails();

                        prepareNotificationMessage(OrderId,message);

                    //    Toast.makeText(getContext(), "prepareNotificationMessage(OrderId,message)"+ "Checked", Toast.LENGTH_SHORT).show();

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







    //when seller change order status send data
    private void prepareNotificationMessage(String orderId, String message){

        //when user Place order,send notification to user

        //prepare data for notifications
        String NOTIFICATION_TOPIC = "/topics" + Constants.FCM_TOPIC; //must be same as subscriber by user
        String NOTIFICATION_TITLE = "Your Order " + orderId;
        String NOTIFICATION_MESSAGE = ""+message;
        String NOTIFICATION_TYPE = "OrderStatusChanged";

        //now perpare jason (What to send and where to send)

        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();

        try {
            //what to send
            notificationBodyJo.put("notificationType",NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid",OrderBy);
            notificationBodyJo.put("sellerUid",firebaseAuth.getUid());
            notificationBodyJo.put("orderId",orderId);
            notificationBodyJo.put("notificationTitle",NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage",NOTIFICATION_MESSAGE);

            //where to send

            notificationJo.put("to",NOTIFICATION_TOPIC); // to all who subscribed to these topic
            notificationJo.put("data",notificationBodyJo);



        }catch (Exception e){

            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        sendFcmNotification(notificationJo);

    }

    private void sendFcmNotification(JSONObject notificationJo) {

        //send volley request

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //notification sent

                Toast.makeText(getContext(), "response on sendfmc jasonrequest", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //notification faild to send

                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();


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

}