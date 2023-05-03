package com.example.flashcart.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Model.ModelPromotionCode;
import com.example.flashcart.Model.ModelUserHomePromoCode;
import com.example.flashcart.R;

import java.util.ArrayList;

public class AdaptorPromotionCodeUserHomePage extends RecyclerView.Adapter<AdaptorPromotionCodeUserHomePage.HolderCartItem> {

    private Context context;
    private ArrayList<ModelUserHomePromoCode> promotionCodeArrayList;

    public AdaptorPromotionCodeUserHomePage(Context context, ArrayList<ModelUserHomePromoCode> promotionCodeArrayList1) {
        this.context = context;
        this.promotionCodeArrayList = promotionCodeArrayList1;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_homepromotion_list,parent,false);

        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, int position) {

        //get data


        ModelUserHomePromoCode modelUserHomePromoCode = promotionCodeArrayList.get(position);

        String promocode = modelUserHomePromoCode.getPromoCode();
        String promodescription = modelUserHomePromoCode.getPromoDescription();



        holder.promocode1.setText(promocode);
        holder.promodescription1.setText(promodescription);


    }



    @Override
    public int getItemCount() {
        return promotionCodeArrayList.size();
    }

    //view holder class

    class HolderCartItem extends RecyclerView.ViewHolder{



        ImageView promoImage;

        //init view
        TextView promodescription1,promocode1;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            promoImage = itemView.findViewById(R.id.promoImage);
            promodescription1 = itemView.findViewById(R.id.promodescription1);
            promocode1 = itemView.findViewById(R.id.promocode1);



        }
    }

}





