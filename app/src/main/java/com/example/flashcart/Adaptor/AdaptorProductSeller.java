package com.example.flashcart.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.R;
import com.example.flashcart.SellerPage.FilterProduct;
import com.example.flashcart.Model.ModelProduct;
import com.example.flashcart.SellerPage.SellerEditProduct_fragmnet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailbottomSheet(modelProduct);
            }
        });


    }

    private void detailbottomSheet(ModelProduct modelProduct) {


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view now

        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheetproduct_detail,null);
        bottomSheetDialog.setContentView(view);

        ImageButton bacbtn = view.findViewById(R.id.bacBtn);
        ImageButton deletebtn = view.findViewById(R.id.deleteBtn);
        ImageButton editbtn = view.findViewById(R.id.editBtn);
        ImageView productimage = view.findViewById(R.id.producticonTv);
        TextView discountnote = view.findViewById(R.id.discountNoteTv);
        TextView title = view.findViewById(R.id.titleTv);
        TextView category = view.findViewById(R.id.categoryTv);
        TextView discription = view.findViewById(R.id.descriptionTv);
        TextView orignalPrice = view.findViewById(R.id.orignalpriceTv);
        TextView discountprice = view.findViewById(R.id.discountPriceTv);

        //here we will get the data

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



        title.setText("Product Name : "+Title);
        discountnote.setText("Discount Note : "+discountNote);
        category.setText("Category : "+productCategory);
        discription.setText("Description : "+productDescription);
        orignalPrice.setText("$"+OrignalPrice);
        discountprice.setText("$"+discountPrice);

        if(discountAvailable.equals(true)){
            //product is on orignal price
            discountnote.setVisibility(View.VISIBLE);
            discountprice.setVisibility(View.VISIBLE);
            orignalPrice.setPaintFlags(orignalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); //adding strick through orignal price

        }else {
            //product is not orignal price

            discountprice.setVisibility(View.GONE);
            discountnote.setVisibility(View.GONE);

        }
        try {

            Picasso.get().load(Icon).placeholder(R.drawable.baseline_shopping_cart_24).into(productimage);

        }catch (Exception e){

            productimage.setImageResource(R.drawable.baseline_add_shopping_cart_24);

        }


        bottomSheetDialog.show();

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                Intent intent = new Intent(context, SellerEditProduct_fragmnet.class);
                intent.putExtra("ProductId",id);
                context.startActivity(intent);

            }
        });

        bacbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bottomSheetDialog.dismiss();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are your sure you want to delete Product ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(id); //here we will delete product by id
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                   //dismiss
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


    }

    private void deleteProduct(String id) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Products").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);


                        builder.setTitle("Sucess")
                                .setMessage("Your Item Deleted Sucessfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
