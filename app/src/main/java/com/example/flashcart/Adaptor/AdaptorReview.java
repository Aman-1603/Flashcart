package com.example.flashcart.Adaptor;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.flashcart.Model.ModelReview;
import com.example.flashcart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class AdaptorReview  extends RecyclerView.Adapter<AdaptorReview.HolderReview>{


    private Context context;
    private ArrayList<ModelReview> reviewArrayList;

    public AdaptorReview(Context context, ArrayList<ModelReview> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public HolderReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_reviews,parent,false);
        return new HolderReview(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderReview holder, int position) {

        ModelReview modelReview = reviewArrayList.get(position);
        String uid = modelReview.getUid();
        String rating = modelReview.getRatings();
        String timestamp = modelReview.getTimestamps();
        String review = modelReview.getReview();



        loadUserDetails(modelReview,holder);



        //now need to convert timestamp to proper formate

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));

        String dateformate = DateFormat.format("dd/MM/yyyy",calendar).toString();

        holder.ratingBar.setRating(Float.parseFloat(rating));
        holder.reviewTv.setText(review);
        holder.dateTv.setText(dateformate);


    }

    private void loadUserDetails(ModelReview modelReview, HolderReview holder) {


        String uid = modelReview.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        String name = ""+datasnapshot.child("name").getValue();
                        String profileimage = ""+datasnapshot.child("profileImage").getValue();

                        holder.nameTv.setText(name);

                        try {

                            Picasso.get().load(profileimage).placeholder(R.drawable.baseline_person_24).into(holder.profileTv);

                        }catch (Exception e){

                            holder.profileTv.setImageResource(R.drawable.baseline_person_24);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    class HolderReview extends RecyclerView.ViewHolder{

        //now ui view of layout

        CircularImageView profileTv;
        TextView nameTv,dateTv,reviewTv;
        RatingBar ratingBar;

        public HolderReview(@NonNull View itemView) {
            super(itemView);

            profileTv = itemView.findViewById(R.id.UserprofileTV);
            nameTv = itemView.findViewById(R.id.username);
            dateTv = itemView.findViewById(R.id.reviewdate);
            reviewTv = itemView.findViewById(R.id.reviewtext);
            ratingBar = itemView.findViewById(R.id.ratingBarTV);


        }
    }

}
