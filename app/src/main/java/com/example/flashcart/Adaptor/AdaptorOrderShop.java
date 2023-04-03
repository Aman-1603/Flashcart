package com.example.flashcart.Adaptor;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Filter.FilterOrderShop;
import com.example.flashcart.Model.ModelOrderShop;
import com.example.flashcart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdaptorOrderShop extends RecyclerView.Adapter<AdaptorOrderShop.HolderOrderShop> implements Filterable {


    private Context context;
    public ArrayList<ModelOrderShop> orderShopArrayList,FilterList;
    private FilterOrderShop filter;

    public AdaptorOrderShop(Context context, ArrayList<ModelOrderShop> orderShopArrayList) {
        this.context = context;
        this.orderShopArrayList = orderShopArrayList;
        this.FilterList = orderShopArrayList;
    }

    @NonNull
    @Override
    public HolderOrderShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_order_seller,parent,false);

        return new HolderOrderShop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderShop holder, int position) {

        //now get data position

        ModelOrderShop modelOrderShop = orderShopArrayList.get(position);

        String orderId = modelOrderShop.getOrderId();
        String orderBy = modelOrderShop.getOrderBy();
        String orderCost = modelOrderShop.getOrderFinalSubTotal();
        String orderStatus = modelOrderShop.getOrderStatus();
        String orderTime = modelOrderShop.getOrderTime();
        String orderTo = modelOrderShop.getOrderTo();


        loadShopInfo(modelOrderShop,holder);

        holder.orderTotalamountTv.setText("Amount ₹"+orderCost);
        holder.orderstatusTv.setText(orderStatus);
        holder.orderIdTv.setText("Order Id : "+orderId);
        holder.orderstatusTv.setTextColor(context.getResources().getColor(R.color.purple_200));

        if (orderStatus.equals("In Progress")){
            holder.orderstatusTv.setTextColor(context.getResources().getColor(R.color.purple_200));
        } else if (orderStatus.equals("Complete")) {
            holder.orderstatusTv.setTextColor(context.getResources().getColor(R.color.colorGreen1));
        } else if (orderStatus.equals("Cancelled")) {

            holder.orderstatusTv.setTextColor(context.getResources().getColor(R.color.red));
        }



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderTime));
        String formateDate = DateFormat.format("dd/MM/yyyy",calendar).toString();

        holder.orderdateTv.setText(formateDate);

    }

    private void loadShopInfo(ModelOrderShop modelOrderShop, HolderOrderShop holder) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(modelOrderShop.getOrderBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        String email = ""+datasnapshot.child("email").getValue();

                        holder.useremailTv.setText(email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return orderShopArrayList.size();
    }

    @Override
    public Filter getFilter() {

        if (filter == null){

            filter = new FilterOrderShop(this,FilterList);

        }

        return filter;
    }

    class HolderOrderShop extends RecyclerView.ViewHolder{


        TextView orderIdTv,useremailTv,orderTotalamountTv,orderdateTv,orderstatusTv;
        ImageView orderNextTv;

        public HolderOrderShop(@NonNull View itemView) {
            super(itemView);


            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            useremailTv = itemView.findViewById(R.id.useremailTv);
            orderTotalamountTv = itemView.findViewById(R.id.orderTotalamountTv);
            orderdateTv = itemView.findViewById(R.id.orderdateTv);
            orderNextTv = itemView.findViewById(R.id.orderNextTv);
            orderstatusTv = itemView.findViewById(R.id.orderstatusTv);

        }
    }

}
