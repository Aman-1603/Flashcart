package com.example.flashcart.UserPage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.flashcart.Adaptor.AdaptorReview;
import com.example.flashcart.Model.ModelReview;
import com.example.flashcart.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class User_Shop_AllReview_fragment extends Fragment {

    BarChart barChart;

    String ShopUid;
    CircularImageView profileTv;
    TextView shopnameTv,TotalReviews,finalratingTv;
    RatingBar ratingBar;
    RecyclerView reviewRv;

    FirebaseAuth firebaseAuth;

    ArrayList<ModelReview> reviewArrayList;
    AdaptorReview adaptorReview;



    public User_Shop_AllReview_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user__shop__all_review_fragment, container, false);

        barChart = view.findViewById(R.id.barChart);
        profileTv = view.findViewById(R.id.shopimage);
        shopnameTv = view.findViewById(R.id.shopname);
        TotalReviews = view.findViewById(R.id.totalreview);
        ratingBar = view.findViewById(R.id.ratingBar);
        reviewRv = view.findViewById(R.id.allreviewrecycle);
        finalratingTv = view.findViewById(R.id.finalrating);



        firebaseAuth = FirebaseAuth.getInstance();


        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            ShopUid = bundle.getString("shopUid");
        }



        //bar graph

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 5f));
        entries.add(new BarEntry(1f, 10f));
        entries.add(new BarEntry(2f, 8f));
        entries.add(new BarEntry(3f, 3f));
        entries.add(new BarEntry(4f, 12f));



        BarDataSet dataSet = new BarDataSet(entries, "Label");

        // Set custom colors for each bar
        dataSet.setColors(new int[] { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA });




        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();

        //bar graph ends here



        loadShopDetails();  //for shop name,rating and all

        loadReviews();






        return view;
    }


    float ratingSum = 0;
    private void loadReviews() {

        reviewArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(ShopUid).child("Ratings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {


                        reviewArrayList.clear();
                        ratingSum = 0;

                        for (DataSnapshot ds: datasnapshot.getChildren()){


                            float rating = Float.parseFloat(""+ds.child("ratings").getValue());
                            ratingSum = ratingSum + rating;

                            ModelReview modelReview = ds.getValue(ModelReview.class);
                            reviewArrayList.add(modelReview);

                        }

                        adaptorReview = new AdaptorReview(getContext(),reviewArrayList);

                        reviewRv.setAdapter(adaptorReview);

                        long numberofReviews = datasnapshot.getChildrenCount();
                        float avgrating = ratingSum/numberofReviews;


                        TotalReviews.setText(""+numberofReviews + " Reviews");
                        finalratingTv.setText(""+avgrating + "/5");

                        ratingBar.setRating(avgrating);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void loadShopDetails() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(ShopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        String shopname = ""+datasnapshot.child("shopName").getValue();
                        String shopProfileImage = ""+datasnapshot.child("profileImage").getValue();


                        shopnameTv.setText(shopname);

                        try {

                            Picasso.get().load(shopProfileImage).placeholder(R.drawable.baseline_person_24).into(profileTv);

                        }catch (Exception e){

                            profileTv.setImageResource(R.drawable.baseline_person_24);

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}