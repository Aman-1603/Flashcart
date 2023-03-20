package com.example.flashcart.Adaptor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptorCartItem extends RecyclerView.Adapter<AdaptorCartItem.HolderCartItem> {

    private Context context;
    private ArrayList<ModelCartItemRecieve> cartItems;

    public AdaptorCartItem(Context context, ArrayList<ModelCartItemRecieve> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_item_cart,parent,false);

        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, int position) {

        //get data

        ModelCartItemRecieve modelCartItemRecieve = cartItems.get(position);

        String title = modelCartItemRecieve.getTitle();
        String price = modelCartItemRecieve.getPrice();
        String quantity = modelCartItemRecieve.getQuantity();
        String producticon = modelCartItemRecieve.getProductIcon();


        //set data

        holder.itemtitle1TV.setText(""+title);
        holder.itemprice1TV.setText(""+price);
        holder.quantity1tv.setText(""+quantity);


        try{

            Picasso.get().load(producticon).placeholder(R.drawable.baseline_add_shopping_black_cart_24).into(holder.productIconTv);


        }catch (Exception e){

            holder.productIconTv.setImageResource(R.drawable.baseline_add_shopping_black_cart_24);

        }



    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    //view holder class

    class HolderCartItem extends RecyclerView.ViewHolder{



        //init view
        private TextView itemtitle1TV,itemprice1TV,quantity1tv;
        ImageView productIconTv;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            itemtitle1TV = itemView.findViewById(R.id.itemtitleTv);
            itemprice1TV = itemView.findViewById(R.id.itempriceTv);
            quantity1tv = itemView.findViewById(R.id.itemQuantityTv);
            productIconTv = itemView.findViewById(R.id.itemIconTv);


        }
    }

}
