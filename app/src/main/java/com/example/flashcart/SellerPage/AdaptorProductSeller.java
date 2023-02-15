package com.example.flashcart.SellerPage;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.example.flashcart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptorProductSeller extends RecyclerView.Adapter<AdaptorProductSeller.HolderProductSeller> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productList,filterList;

    private FilterProduct filter;

    public AdaptorProductSeller(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    @NonNull
    @Override
    public HolderProductSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout

        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller,parent,false);
        return new HolderProductSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductSeller holder, int position) {

        //here we will get the data

        ModelProduct modelProduct = productList.get(position);
        String id = modelProduct.getProductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getProductDiscountAvailable();
        String discountNote = modelProduct.getProductDiscountNote();
        String discountPrice = modelProduct.getProductDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String productDescription = modelProduct.getProductDescription();
        String Icon = modelProduct.getProductIcon();
        String Quantity = modelProduct.getProductQuantity();
        String Title = modelProduct.getProductTitle();
        String OrignalPrice = modelProduct.getProductPrice();
        String TimeStamp = modelProduct.getTimeStamp();


        //now we will set the data

        holder.titleTv.setText(Title);
        holder.quantityTv.setText(Quantity);
        holder.discountPriceTv.setText("$"+discountPrice);
        holder.orignalPriceTv.setText("$"+OrignalPrice);

        if(discountAvailable.equals(true)){
            //product is on orignal price
            holder.discountPriceTv.setVisibility(View.VISIBLE);
            //holder.discountNote ?? pending for future
            holder.orignalPriceTv.setPaintFlags(holder.orignalPriceTv.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); //adding strick through orignal price

        }else {
            //product is not orignal price

            holder.discountPriceTv.setVisibility(View.GONE);
            //holder.discountNote ?? pending for future

        }
        try {

            Picasso.get().load(Icon).placeholder(R.drawable.baseline_shopping_cart_24).into(holder.productIconTv);

        }catch (Exception e){

            holder.productIconTv.setImageResource(R.drawable.baseline_add_shopping_cart_24);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user click on item then the specific item detail page should be visible

                Toast.makeText(context, "These Page will be done in future", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    @Override
    public Filter getFilter() {

        if(filter == null){

            filter = new FilterProduct(this,filterList);
        }

        return filter;
    }

    class HolderProductSeller extends RecyclerView.ViewHolder{

    //holds view of recycler view

     ImageView productIconTv;
     TextView titleTv,quantityTv,discountPriceTv,orignalPriceTv;
    public HolderProductSeller(@NonNull View itemView) {
        super(itemView);


        productIconTv = itemView.findViewById(R.id.productIconTv);
        titleTv = itemView.findViewById(R.id.titleTv);
        quantityTv = itemView.findViewById(R.id.quantityTv);
        discountPriceTv = itemView.findViewById(R.id.discountPriceTv);
        orignalPriceTv = itemView.findViewById(R.id.orignalPriceTv);

    }

    }
}
