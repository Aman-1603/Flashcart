package com.example.flashcart.Adaptor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Model.ModelShop;
import com.example.flashcart.R;
import com.example.flashcart.UserPage.UserShowShopDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptorShopUser extends RecyclerView.Adapter<AdaptorShopUser.HolderShope> {

    private Context context;
    private ArrayList<ModelShop> shopsList;

    public AdaptorShopUser(Context context, ArrayList<ModelShop> shopsList) {
        this.context = context;
        this.shopsList = shopsList;
    }

    @NonNull
    @Override
    public HolderShope onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout row_shop

        View view = LayoutInflater.from(context).inflate(R.layout.row_shop,parent,false);
        return new HolderShope(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderShope holder, int position) {

        //now here we will get the data

        ModelShop modelShop = shopsList.get(position);
        String accountType = modelShop.getAccountType();
        String address = modelShop.getAddress();
        String city = modelShop.getCity();
        String country= modelShop.getCountry();
        String deliveryFee= modelShop.getDeliveryFees();
        String email= modelShop.getEmail();
        String latitude= modelShop.getLatitude();
        String longitude= modelShop.getLongitude();
        String name= modelShop.getName();
        String phone= modelShop.getPhone();
        String uid= modelShop.getUid();
        String timestamp= modelShop.getTimestamp();
        String shopOpen= modelShop.getShopOpen();
        String state= modelShop.getState();
        String profileImage= modelShop.getProfileImage();
        String shopName= modelShop.getShopName();
        String online= modelShop.getOnline();


        //now set the data

        holder.shopnameTV.setText(shopName);
        holder.phoneTV.setText(phone);
        holder.addressTV.setText(address);
        if (online.equals("true")){
            //shop owner is online
            holder.onlineTV.setVisibility(View.VISIBLE);
        }else {
            //shop owner is not online
            holder.onlineTV.setVisibility(View.GONE);
        }

        //check if shop is open

        if(shopOpen.equals("true")){
            holder.shopcloseTV.setVisibility(View.GONE);
        }else {
            holder.shopcloseTV.setVisibility(View.VISIBLE);
        }


        try {

            Picasso.get().load(profileImage).placeholder(R.drawable.baseline_store_24).into(holder.shopTV);

        }catch (Exception e){

            holder.shopTV.setImageResource(R.drawable.baseline_store_24);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString("shopUid",uid); // add the data to the bundle using a key-value pair

                // Create an instance of the new fragment
                UserShowShopDetail newFragment = new UserShowShopDetail();

                // Set the arguments for the new fragment to the bundle
                newFragment.setArguments(bundle);

                // Get the fragment manager
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

                // Begin a new transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the previous fragment with the new fragment
                fragmentTransaction.replace(R.id.Frame_layout, newFragment);

                // Add the transaction to the back stack so the user can navigate back to the previous fragment
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();


//
//                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
//
//                // Begin a new transaction
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                // Create an instance of the new fragment
//                UserShowShopDetail newFragment = new UserShowShopDetail();
//
//                // Replace the previous fragment with the new fragment
//                fragmentTransaction.replace(R.id.Frame_layout, newFragment);
//
//                // Commit the transaction
//                fragmentTransaction.commit();

            }
        });


    }

    private void replaceFragment(UserShowShopDetail fragment) {


//        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        // Create an instance of the new fragment
//        Fragment newFragment = new Fragment();
//
//        // Add the new fragment to the transaction
//        fragmentTransaction.add(R.id.Frame_layout, fragment);
//
//        // Commit the transaction
//        fragmentTransaction.commit();

    }


    @Override
    public int getItemCount() {
        return shopsList.size();
    }
//view holder

    class HolderShope extends RecyclerView.ViewHolder{

        //ui view of row_shop.xml

         ImageView shopTV,onlineTV,nextTV;
         TextView shopcloseTV,shopnameTV,phoneTV,addressTV;
         RatingBar ratingBarTV;

        public HolderShope(@NonNull View itemView) {
            super(itemView);

            //initialize uid view

            shopTV = itemView.findViewById(R.id.shopTV);
            onlineTV = itemView.findViewById(R.id.onlineTV);
            nextTV = itemView.findViewById(R.id.nextTV);
            shopcloseTV = itemView.findViewById(R.id.shopcloseTV);
            shopnameTV = itemView.findViewById(R.id.shopnameTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            ratingBarTV = itemView.findViewById(R.id.ratingBarTV);



        }
    }


}
