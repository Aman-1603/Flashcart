package com.example.flashcart.UserPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcart.LoginActivity;
import com.example.flashcart.R;
import com.example.flashcart.databinding.ActivityMainUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainUserActivity extends AppCompatActivity {

    private TextView nameTV;
    private ImageButton logoutbtn;

    private FirebaseAuth firebaseAuth;

    ActivityMainUserBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding = ActivityMainUserBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //bydefault fragment would be home page of use

        replaceFragment(new UserHomeFragment());

        binding.bottomnavigationBar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.Home:
                    replaceFragment(new UserHomeFragment());
                    break;
                case R.id.Category:

                    replaceFragment(new UserShowShop());

                    break;
                case R.id.Notification:
                    break;
                case R.id.Cart:
                    break;
                case R.id.Account:
                    replaceFragment(new ProfileFragment());
                    break;

            }


            return true;
        });

        //currently we will not use it for now to diaplay name to home page

//        nameTV = findViewById(R.id.nametv);
//        logoutbtn = findViewById(R.id.logoutbtn);

//        firebaseAuth = FirebaseAuth.getInstance();
//        CheckUser();


//        logoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckUser();
//            }
//        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void CheckUser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
            finish();
        }else{
            loadMyInfo();
        }
    }

    private void loadMyInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot ds : datasnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();

                            nameTV.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}