package com.example.flashcart.Adaptor;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Filter.FilterProductUser;
import com.example.flashcart.Model.ModelProduct;
import com.example.flashcart.Model.ModelShop;
import com.example.flashcart.R;
import com.example.flashcart.UserPage.UserProductPage;
import com.example.flashcart.UserPage.UserShowShopDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AdaptorUserShopProduct extends RecyclerView.Adapter<AdaptorUserShopProduct.HolderUserShopProduct> implements Filterable {

     Context context;
    public ArrayList<ModelProduct> productsList,filterList;
    FilterProductUser filter;

    public AdaptorUserShopProduct(Context context, ArrayList<ModelProduct> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.filterList = productsList;
    }

    @NonNull
    @Override
    public HolderUserShopProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout

        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller2,parent,false);

        return new HolderUserShopProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUserShopProduct holder, int position) {

        //now get the data

        ModelProduct modelProduct = productsList.get(position);

        String discountAvailable = modelProduct.getProductDiscountAvailable();
        String discountNote = modelProduct.getProductDiscountNote();
        String discountprice = modelProduct.getProductDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String orignalPrice = modelProduct.getProductPrice();
        String productDescription = modelProduct.getProductDescription();
        String productTitle = modelProduct.getProductTitle();
        String productQuantity = modelProduct.getProductQuantity();
        String productId = modelProduct.getProductId();
        String timestamp = modelProduct.getTimeStamp();
        String productIcon = modelProduct.getProductIcon();

        String uid = modelProduct.getUid();




        //now actual set data

        holder.titleTv.setText(productTitle);
//        holder.discountNoteTv.setText(discountNote);
        holder.descriptionTv.setText(productDescription);
        holder.orignalPriceTv.setText(orignalPrice);
        holder.discountPriceTv.setText(discountprice);

        if (discountAvailable.equals("true")){

            //product is on discount

            holder.discountPriceTv.setVisibility(View.VISIBLE);
            holder.discountNoteTv.setVisibility(View.VISIBLE);
            holder.orignalPriceTv.setPaintFlags(holder.orignalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }else {

            //then product is not on discount

            holder.discountPriceTv.setVisibility(View.GONE);
//            holder.discountNoteTv.setVisibility(View.GONE);
            holder.orignalPriceTv.setPaintFlags(0);

        }


        try{

            Picasso.get().load(productIcon).placeholder(R.drawable.baseline_add_shopping_black_cart_24).into(holder.productIconTv);


        }catch (Exception e){

            holder.productIconTv.setImageResource(R.drawable.baseline_add_shopping_black_cart_24);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show Product whole detail

                Bundle bundle = new Bundle();
                bundle.putString("ItemUid",productId);// add the data to the bundle using a key-value pair
                bundle.putString("ShopUid",uid);


                // Create an instance of the new fragment
                UserProductPage newFragment = new UserProductPage();

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

            }
        });



    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProductUser(this,filterList);
        }
        return filter;
    }

    class HolderUserShopProduct extends RecyclerView.ViewHolder{

        //ui view

         ImageView productIconTv;
         TextView discountNoteTv,titleTv,descriptionTv,addToCartTv,discountPriceTv,orignalPriceTv;



        public HolderUserShopProduct(@NonNull View itemView) {
            super(itemView);

            productIconTv = itemView.findViewById(R.id.productIconTv);
            discountNoteTv = itemView.findViewById(R.id.discountNoteTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            addToCartTv = itemView.findViewById(R.id.addtocartTv);
            discountPriceTv = itemView.findViewById(R.id.discountPriceTv);
            orignalPriceTv = itemView.findViewById(R.id.orignalPriceTv);




        }
    }

}
