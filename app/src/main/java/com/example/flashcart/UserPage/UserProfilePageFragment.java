package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.flashcart.R;


public class UserProfilePageFragment extends Fragment {


    Button profilebutton1;

    public UserProfilePageFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile_page, container, false);

        profilebutton1 = view.findViewById(R.id.profilebutton1);

        profilebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UserOrderPage newFragment = new UserOrderPage();


                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();

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




        return view;
    }
}