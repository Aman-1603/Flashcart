package com.example.flashcart.Adaptor;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcart.Model.ModelOrderUser;
import com.example.flashcart.R;
import com.example.flashcart.UserPage.UserOrderDetailFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdaptorOrderUser extends RecyclerView.Adapter<AdaptorOrderUser.HolderOrderUser> {

    private Context context;
    private ArrayList<ModelOrderUser> orderUserList;


    public AdaptorOrderUser(Context context, ArrayList<ModelOrderUser> orderUserList) {
        this.context = context;
        this.orderUserList = orderUserList;
    }

    @NonNull
    @Override
    public HolderOrderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_order_user,parent,false);

        return new HolderOrderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderUser holder, int position) {

        ModelOrderUser modelOrderUser = orderUserList.get(position);

        String orderId = modelOrderUser.getOrderId();
        String orderBy = modelOrderUser.getOrderBy();
        String orderCost = modelOrderUser.getOrderFinalSubTotal();
        String orderStatus = modelOrderUser.getOrderStatus();
        String orderTime = modelOrderUser.getOrderTime();
        String orderTo = modelOrderUser.getOrderTo();

        //get shop info

        loadShopInfo(modelOrderUser,holder);




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


//        now we will convert timestamp to proper formate

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderTime));
        String formateDate = DateFormat.format("dd/MM/yyyy",calendar).toString();

        holder.orderdateTv.setText(formateDate);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString("orderTo",orderTo);
                bundle.putString("orderId",orderId);

                // Create an instance of the new fragment
                UserOrderDetailFragment newFragment = new UserOrderDetailFragment();


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

    private void loadShopInfo(ModelOrderUser modelOrderUser, HolderOrderUser holder) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(modelOrderUser.getOrderTo())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        String shopName = ""+datasnapshot.child("shopName").getValue();

                        holder.ordershopTv.setText(shopName);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return orderUserList.size();
    }

    class HolderOrderUser extends RecyclerView.ViewHolder{


        //view of layout

        TextView orderIdTv,ordershopTv,orderTotalamountTv,orderdateTv,orderstatusTv;
        ImageView orderNextTv;

        public HolderOrderUser(@NonNull View itemView) {
            super(itemView);

            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            ordershopTv = itemView.findViewById(R.id.ordershopTv);
            orderTotalamountTv = itemView.findViewById(R.id.orderTotalamountTv);
            orderdateTv = itemView.findViewById(R.id.orderdateTv);
            orderNextTv = itemView.findViewById(R.id.orderNextTv);
            orderstatusTv = itemView.findViewById(R.id.orderstatusTv);
        }
    }

}
