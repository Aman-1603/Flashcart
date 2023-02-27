package com.example.flashcart.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Model.ModelShop;
import com.example.flashcart.R;
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
