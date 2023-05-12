package com.example.flashcart.UserPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.R;
import com.example.flashcart.categorylist.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;


public class UserProfilePageFragment extends Fragment {


    //setting page data

    TextView notificationStatusTv;
    SwitchCompat notificationswitch;

    RelativeLayout big_wishlist;


    Button profilebutton4,profilebutton2,return_home;



    boolean isChecked = false;



    private static final String enableMessage = "Notification are enabled";
    private static final String disableMessage = "Notification are disabled";


    //now need to save what is choose in switchbutton

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;


    //upto these setting page



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
        notificationStatusTv = view.findViewById(R.id.notificationStatusTv);
        notificationswitch = view.findViewById(R.id.notificationswitch);
        profilebutton4 = view.findViewById(R.id.profilebutton4);

        big_wishlist = view.findViewById(R.id.big_wishlist);
        profilebutton2 = view.findViewById(R.id.profilebutton2);
        return_home = view.findViewById(R.id.return_home);



        big_wishlist.setOnClickListener(new View.OnClickListener() {
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



        profilebutton2.setOnClickListener(new View.OnClickListener() {
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



        return_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserHomeFragment newFragment = new UserHomeFragment();


                // Get the fragment manager
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

        profilebutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User_WishList_Fragment newFragment = new User_WishList_Fragment();


                // Get the fragment manager
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



        //setting


        sharedPreferences = getActivity().getSharedPreferences("SETTINGS_SP", Context.MODE_PRIVATE);

        isChecked = sharedPreferences.getBoolean("FCM_ENABLED",false);

        notificationswitch.setChecked(isChecked);

        if (isChecked){

            notificationStatusTv.setText(enableMessage);

        }else {

            notificationStatusTv.setText(disableMessage);

        }


        //now checked last selected button

        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    subscribeToTopic();
                }else {
                    unsubscribeToTopic();
                }

            }
        });

        //upto these


        return view;
    }

    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //topic subscribed successfully


                        spEditor = sharedPreferences.edit();
                        spEditor.putBoolean("FCM_ENABLED",true);
                        spEditor.apply();

                        notificationStatusTv.setText(""+enableMessage);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //not subscribed

                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });




    }

    private void unsubscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //topic unsubscribed successfully

                        spEditor = sharedPreferences.edit();
                        spEditor.putBoolean("FCM_ENABLED",true);
                        spEditor.apply();

                        notificationStatusTv.setText(""+disableMessage);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //not subscribed

                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

}