package com.example.flashcart.Adaptor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.Model.ModelWishList;
import com.example.flashcart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptorWishList extends RecyclerView.Adapter<AdaptorWishList.HolderCartItem> {

    private Context context;
    private ArrayList<ModelWishList> cartItems;

    public AdaptorWishList(Context context, ArrayList<ModelWishList> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_userwishlist,parent,false);

        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, int position) {

        //get data

        ModelWishList modelWishList = cartItems.get(position);

//        String pid = modelCartItemRecieve.getItemUid();
//        String title = modelCartItemRecieve.getTitle();
//        String price = modelCartItemRecieve.getPrice();
//        String quantity = modelCartItemRecieve.getQuantity();
//        String producticon = modelCartItemRecieve.getProductIcon();



        String title = modelWishList.getTitle();
        String price = modelWishList.getPriceEach();
        String producticon = modelWishList.getProductIcon();

//
//        //set data
//
        holder.Title.setText(""+title);
        holder.price.setText("₹"+price);


        try{

            Picasso.get().load(producticon).placeholder(R.drawable.baseline_add_shopping_black_cart_24).into(holder.image);


        }catch (Exception e){

            holder.image.setImageResource(R.drawable.baseline_add_shopping_black_cart_24);

        }


    }



//    delete not required in these page


//    private void delete(String pid) {
//
//
//
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference.child(firebaseAuth.getUid()).child("AddtoCart").child(pid).removeValue()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//
//                        Toast.makeText(context, "Product deleted succesfully", Toast.LENGTH_SHORT).show();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    //view holder class

    class HolderCartItem extends RecyclerView.ViewHolder{



        //init view
        TextView Title,price;
        ImageView image;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.wishlistTitle);
            price = itemView.findViewById(R.id.wishlistprice);
            image = itemView.findViewById(R.id.wishlistImage);



        }
    }

}






