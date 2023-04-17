package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flashcart.Adaptor.AdaptorCartItem;
import com.example.flashcart.Adaptor.AdaptorWishList;
import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.Model.ModelWishList;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserHomeFragment extends Fragment {


    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;


    AdaptorWishList adaptorWishList;
    ArrayList<ModelWishList> list;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        //for category
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);


        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.homewishlistRv);

        //up to these category

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();


        slideModels.add(new SlideModel(R.drawable.oneplus1,"Discount On Mobile Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.onplus2,"Offer ON Laptop", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.onplus3,"New Addition in Headphone", ScaleTypes.CENTER_CROP));


        imageSlider.setImageList(slideModels);

        imageSlider.setImageList(slideModels);



        loadUserWishList();


        return view;
    }

    private void loadUserWishList() {


        list = new ArrayList<>();
        adaptorWishList = new AdaptorWishList(getContext(),list);
        recyclerView.setAdapter(adaptorWishList);


        try {


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("UserWishList")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {


                            for (DataSnapshot ds : datasnapshot.getChildren()) {
                                ModelWishList modelWishList = ds.getValue(ModelWishList.class);


                                Log.d("one",modelWishList.getTitle());
                                Log.d("two",modelWishList.getPriceEach());
                                Log.d("three",modelWishList.getProductIcon());
                                Log.d("data from model", String.valueOf(modelWishList));

                                list.add(modelWishList);
                            }

                            adaptorWishList.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }catch (Exception e){

            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }


}