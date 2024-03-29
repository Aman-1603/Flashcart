package com.example.flashcart.SellerPage;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.R;
import com.example.flashcart.categorylist.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

public class Seller_Account_page_Fragment extends Fragment {


    TextView notificationStatusTv;
    SwitchCompat notificationswitch;
    FirebaseAuth firebaseAuth;

    Button Promotionbtn;

    boolean isChecked = false;



    //setting

    private static final String enableMessage = "Notification are enabled";
    private static final String disableMessage = "Notification are disabled";

    //setting up to these




    //now need to save what is choose in switchbutton

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    public Seller_Account_page_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller__account_page_, container, false);


        notificationStatusTv = view.findViewById(R.id.notificationStatusTv);
        notificationswitch = view.findViewById(R.id.notificationswitch);
        Promotionbtn = view.findViewById(R.id.Promotionbtn);


        firebaseAuth = FirebaseAuth.getInstance();

        //init shared preference

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


        Promotionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPromotionPage();

            }
        });


        return view;
    }

    private void showPromotionPage() {

        SellerPromotionCodeFragment newFragment = new SellerPromotionCodeFragment();


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



//required for notification message : fmc server