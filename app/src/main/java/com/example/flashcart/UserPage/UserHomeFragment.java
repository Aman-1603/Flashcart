package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flashcart.Adaptor.AdaptorUserHomeCategory;
import com.example.flashcart.R;
import com.example.flashcart.Adaptor.AdaptorProductSeller;
import com.example.flashcart.SellerPage.ModelProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {

    RecyclerView catRecyclerView;
    //Category recyclerview
    private ArrayList<ModelProduct> productList;

    private AdaptorUserHomeCategory adaptorUserHomeCategory;

    //FireStore
    FirebaseFirestore db;
     FirebaseAuth firebaseAuth;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        //for category
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);


        View root = inflater.inflate(R.layout.fragment_user_home, container, false);

        catRecyclerView = root.findViewById(R.id.rec_home_category);

        firebaseAuth = FirebaseAuth.getInstance();
        LoadCategoryHome();

        //up to these category

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();


        slideModels.add(new SlideModel(R.drawable.oneplus1,"Discount On Mobile Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.onplus2,"Offer ON Laptop", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.onplus3,"New Addition in Headphone", ScaleTypes.CENTER_CROP));


        imageSlider.setImageList(slideModels);

        imageSlider.setImageList(slideModels);


        return view;
    }

    private void LoadCategoryHome() {



        //get all products

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }

                        adaptorUserHomeCategory = new AdaptorUserHomeCategory(getContext(),productList);

                        catRecyclerView.setAdapter(adaptorUserHomeCategory);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

    }


}