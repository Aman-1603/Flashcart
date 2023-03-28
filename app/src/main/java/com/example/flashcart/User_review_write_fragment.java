package com.example.flashcart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.service.controls.actions.FloatAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class User_review_write_fragment extends Fragment {


    CircularImageView imageView;
    TextView shopname;
    RatingBar ratingBar;
    EditText writereview;
    FloatingActionButton submitreview;

    FirebaseAuth firebaseAuth;

    String shopUId;


    public User_review_write_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_review_write_fragment, container, false);


        imageView = view.findViewById(R.id.image1);
        shopname = view.findViewById(R.id.shopnameTv);
        ratingBar = view.findViewById(R.id.ratingbar);
        writereview = view.findViewById(R.id.writewdittext);
        submitreview = view.findViewById(R.id.submitButton);


        firebaseAuth = FirebaseAuth.getInstance();

        //if user has written to review then load it



        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            shopUId = bundle.getString("shopUid");

        }


        loadMyReview();
        loadShopInfo();

        submitreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputdata();

            }
        });

        return view;
    }

    private void loadShopInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                String shopName = ""+datasnapshot.child("shopName").getValue();
                String shopImage = ""+datasnapshot.child("profileImage").getValue();

                //set shop info to ui

                shopname.setText(shopName);

                try {
                    Picasso.get().load(shopImage).placeholder(R.drawable.baseline_store_24).into(imageView);
                }catch (Exception e){

                    imageView.setImageResource(R.drawable.baseline_store_24);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadMyReview() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUId).child("Ratings").child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        if (datasnapshot.exists()){

                            //then get the review detail

                            String uid = ""+datasnapshot.child("uid").getValue();
                            String ratings = ""+datasnapshot.child("ratings").getValue();
                            String review = ""+datasnapshot.child("review").getValue();
                            String timestamps = ""+datasnapshot.child("timestamps").getValue();

                            //set review detail to our UI

                            float myRating = Float.parseFloat(ratings);
                            ratingBar.setRating(myRating);
                            writereview.setText(review);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void inputdata() {

        String ratings = ""+ratingBar.getRating();
        String reviews = writereview.getText().toString().trim();

        //for time of review

        String timestamp = ""+System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("ratings",""+ratings);
        hashMap.put("review",""+reviews);
        hashMap.put("timestamps",""+timestamp);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(shopUId).child("Ratings").child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getContext(), "Review added sucessfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }
}