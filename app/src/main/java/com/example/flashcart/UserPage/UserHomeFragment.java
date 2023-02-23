package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flashcart.R;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {


    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);



        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();


        slideModels.add(new SlideModel(R.drawable.oneplus1,"Discount On Mobile Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.onplus2,"Offer ON Laptop", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.onplus3,"New Addition in Headphone", ScaleTypes.CENTER_CROP));


        imageSlider.setImageList(slideModels);

        imageSlider.setImageList(slideModels);


        return view;
    }



}