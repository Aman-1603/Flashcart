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
import com.example.flashcart.Model.ModelPromotionCode;
import com.example.flashcart.R;

import java.util.ArrayList;

public class AdaptorPromotionCodeShop extends RecyclerView.Adapter<AdaptorPromotionCodeShop.HolderCartItem> {

    private Context context;
    private ArrayList<ModelPromotionCode> promotionCodeArrayList;

    public AdaptorPromotionCodeShop(Context context, ArrayList<ModelPromotionCode> promotionCodeArrayList) {
        this.context = context;
        this.promotionCodeArrayList = promotionCodeArrayList;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_promotioncode_shop,parent,false);

        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, int position) {

        //get data


        ModelPromotionCode modelPromotionCode = promotionCodeArrayList.get(position);

        String id = modelPromotionCode.getId();
        String timestamp = modelPromotionCode.getTimestamp();
        String description = modelPromotionCode.getPromoDescription();
        String promocode = modelPromotionCode.getPromoCode();
        String expireDate = modelPromotionCode.getExpireDate();
        String promoprice = modelPromotionCode.getPromoPrice();
        String minimumOrderPrice = modelPromotionCode.getMinOrderPrice();


        holder.promocodeTv.setText("Promotion Code :"+promocode);
        holder.promomindescTv.setText(description);
        holder.promopriceTv.setText("Discont Price  : "+ "₹"+promoprice);
        holder.promoexpiredateTv.setText("Expire Date :"+ expireDate);
        holder.promominpriceTv.setText("Minimum Order :"+ "₹"+minimumOrderPrice);


    }



    @Override
    public int getItemCount() {
        return promotionCodeArrayList.size();
    }

    //view holder class

    class HolderCartItem extends RecyclerView.ViewHolder{



        //init view
        TextView promocodeTv,promopriceTv,promominpriceTv,promoexpiredateTv,promomindescTv;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            promocodeTv = itemView.findViewById(R.id.promocodeTv);
            promopriceTv = itemView.findViewById(R.id.promopriceTv);
            promominpriceTv = itemView.findViewById(R.id.promominpriceTv);
            promoexpiredateTv = itemView.findViewById(R.id.promoexpiredateTv);
            promomindescTv = itemView.findViewById(R.id.promomindescTv);


        }
    }

}





