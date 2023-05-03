package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flashcart.Adaptor.AdaptorCartItem;
import com.example.flashcart.Adaptor.AdaptorPromotionCodeShop;
import com.example.flashcart.Adaptor.AdaptorPromotionCodeUserHomePage;
import com.example.flashcart.Adaptor.AdaptorWishList;
import com.example.flashcart.Model.ModelCartItemRecieve;
import com.example.flashcart.Model.ModelPromotionCode;
import com.example.flashcart.Model.ModelUserHomePromoCode;
import com.example.flashcart.Model.ModelWishList;
import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserHomeFragment extends Fragment {


    RecyclerView recyclerView,homepromotionlistRv;
    FirebaseAuth firebaseAuth;


    AdaptorWishList adaptorWishList;
    ArrayList<ModelWishList> list;

    ImageSlider imageSlider;

    ImageView adsImageView;


     ArrayList<ModelUserHomePromoCode> promotionCodeArrayList1;
     AdaptorPromotionCodeUserHomePage adaptorPromotionCodeUserHomePage;



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
        imageSlider = view.findViewById(R.id.image_slider);
        adsImageView = view.findViewById(R.id.adsImageView);
        homepromotionlistRv = view.findViewById(R.id.homepromotionlistRv);

        //up to these category

//        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
//        List<SlideModel> slideModels = new ArrayList<>();
//
//
//        slideModels.add(new SlideModel(R.drawable.oneplus1,"Discount On Mobile Items", ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.onplus2,"Offer ON Laptop", ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.onplus3,"New Addition in Headphone", ScaleTypes.CENTER_CROP));
//
//
//        imageSlider.setImageList(slideModels);
//
//        imageSlider.setImageList(slideModels);



        loadBannerImage();

        loadAdsImage();



        loadUserWishList();

        loadAllPromoCodes();


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


    private void loadBannerImage() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
        ref.child("s9n49wBz3CbKU5kOuAkUeI33MC92").child("BannerImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<SlideModel> slideModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("BannerIcon").getValue().toString();
                    String title = snapshot.child("BannerTitle").getValue().toString();
                    slideModels.add(new SlideModel(imageUrl,title,ScaleTypes.CENTER_CROP));

                    Log.d("Image slider",imageUrl);
                }
                imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }


    private void loadAdsImage() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
        ref.child("s9n49wBz3CbKU5kOuAkUeI33MC92").child("AdsImage").child("Section1").child("1683141668627")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

//                            String accountType = ""+ds.child("accountType").getValue();
//                            String address = ""+ds.child("address").getValue();
//                            String city = ""+ds.child("city").getValue();

                        String AdsImage = ""+datasnapshot.child("AdsIcon").getValue();

                        Log.d("Ads Image",AdsImage);



                        try {
                            Picasso.get().load(AdsImage).placeholder(R.drawable.baseline_person_24).into(adsImageView);
                        }catch (Exception e){
                            adsImageView.setImageResource(R.drawable.baseline_person_24);
                        }





                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }



    private void loadAllPromoCodes() {


        promotionCodeArrayList1 = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");

        ref.child("s9n49wBz3CbKU5kOuAkUeI33MC92").child("PromotionCodes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        promotionCodeArrayList1.clear();

                        for (DataSnapshot ds : snapshot.getChildren()){

                            ModelUserHomePromoCode modelPromotionCode = ds.getValue(ModelUserHomePromoCode.class);
                            promotionCodeArrayList1.add(modelPromotionCode);

                            Log.d("Code",modelPromotionCode.getPromoCode());

                        }

                        adaptorPromotionCodeUserHomePage = new AdaptorPromotionCodeUserHomePage(getContext(),promotionCodeArrayList1);
                        homepromotionlistRv.setAdapter(adaptorPromotionCodeUserHomePage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





    }



}