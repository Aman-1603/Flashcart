package com.example.flashcart.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.R;
import com.example.flashcart.SellerPage.ModelProduct;

import java.util.ArrayList;
import java.util.List;

public class AdaptorUserHomeCategory extends RecyclerView.Adapter<AdaptorUserHomeCategory.ViewHolder> {


    private Context context;
    private List<ModelProduct> list;

    public AdaptorUserHomeCategory(Context context, ArrayList<ModelProduct> productList) {

        this.context = context;
        this.list = productList;

    }

    @NonNull
    @Override
    public AdaptorUserHomeCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_homecategorylist,parent,false));    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorUserHomeCategory.ViewHolder holder, int position) {

        ModelProduct modelProduct = list.get(position);
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




        holder.catName.setText(productCategory);
        holder.catImage.setImageResource(R.drawable.baseline_add_shopping_cart_24);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView catImage;
        TextView catName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catImage = itemView.findViewById(R.id.image);

            catName = itemView.findViewById(R.id.label);
        }
    }
}
