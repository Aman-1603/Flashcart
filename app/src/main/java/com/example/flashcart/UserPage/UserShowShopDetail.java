package com.example.flashcart.UserPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flashcart.Adaptor.AdaptorUserShopProduct;
import com.example.flashcart.Model.ModelProduct;
import com.example.flashcart.R;
import com.example.flashcart.categorylist.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

public class UserShowShopDetail extends Fragment {

    TextView nameTV,shopnameTV,shopemailTV,shopphoneTV,shopeAddress,shopopencloseTV,shopdeliveyfeeTV,flterproductTv;
    ImageButton filterproductBtn,backbtn,logoutbtn;
    ImageView shopmapBtn,shopcallBtn;

    private static final int REQUEST_CODE_MAPS = 1;

    String shopUid;
    Double myLatiitude,myLongitude;
    Double ShopLatiitude,ShopLongitude;

    String shopName,shopEmail,shopPhone;

    EditText searchproductEt1;
    private RecyclerView productRv;

    private FirebaseAuth firebaseAuth;


    private ArrayList<ModelProduct> productsList;

    private AdaptorUserShopProduct adaptorUserShopProduct;

    ImageButton reviewButton;

    ImageSlider imageSlider;





    public UserShowShopDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_show_shop_detail, container, false);


        shopnameTV = view.findViewById(R.id.shopnameTV);
        shopphoneTV = view.findViewById(R.id.shopPhoneTV);
        shopemailTV = view.findViewById(R.id.shopemailTV);
        shopeAddress = view.findViewById(R.id.shopeAddressTV);
        shopopencloseTV = view.findViewById(R.id.shopopencloseTV);
        shopdeliveyfeeTV = view.findViewById(R.id.shopdeliveyfeeTV);
        shopopencloseTV = view.findViewById(R.id.shopopencloseTV);
        filterproductBtn = view.findViewById(R.id.filterproductBtn);
        shopmapBtn = view.findViewById(R.id.shopmapBtn);
        shopcallBtn = view.findViewById(R.id.shopcallBtn);
        productRv = view.findViewById(R.id.recyclarItem);
        backbtn = view.findViewById(R.id.backbtn);
        logoutbtn = view.findViewById(R.id.logoutbtn);
        flterproductTv = view.findViewById(R.id.flterproductTv);
        searchproductEt1 = view.findViewById(R.id.searchproductEt1);
        reviewButton = view.findViewById(R.id.reviewbtn);
        imageSlider = view.findViewById(R.id.image_slider);





        //getting bundle data from adaptoeshopuser

        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            shopUid = bundle.getString("shopUid");
        }




        flterproductTv.setText(shopUid);

        firebaseAuth = FirebaseAuth.getInstance();


        loadBannerImage();

        loadMyInfo();
        loadShopDetail();
        loadShopProducts();



        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("shopUid",shopUid); // add the data to the bundle using a key-value pair

                // Create an instance of the new fragment
                User_Shop_AllReview_fragment newFragment = new User_Shop_AllReview_fragment();

                // Set the arguments for the new fragment to the bundle
                newFragment.setArguments(bundle);

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


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        shopcallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhone();
            }
        });

        shopmapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openMap();

                // Create an instance of the new fragment
                UserMapsFragment newFragment = new UserMapsFragment();

                Bundle bundle = new Bundle();

                bundle.putDouble("mylatitude",myLatiitude);
                bundle.putDouble("mylongitude",myLongitude);
                bundle.putDouble("shoplatitude",ShopLatiitude);
                bundle.putDouble("shoplongitude",ShopLongitude);// add the data to the bundle using a key-value pair



                // Set the arguments for the new fragment to the bundle
                newFragment.setArguments(bundle);

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


        searchproductEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    adaptorUserShopProduct.getFilter().filter(s);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        filterproductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Categories:")
                        .setItems(Constants.productcategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected items
                                String selected = Constants.productcategories1[which];
                                flterproductTv.setText(selected);

                                if(selected.equals("All")){
                                    //load all

                                    loadShopProducts();

                                }else{
                                    adaptorUserShopProduct.getFilter().filter(selected);
                                }

                            }
                        }).show();
            }
        });




        return view;
    }



    private void dialPhone() {

        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(shopPhone))));
        Toast.makeText(getContext(), ""+shopPhone, Toast.LENGTH_SHORT).show();

    }

    private void openMap() {

        try {
            String address = "https://maps.google.com/maps?saddr=" + myLatiitude + "," + myLongitude + "&daddr=" + ShopLatiitude + "," + ShopLongitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
            startActivity(intent);



        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }




    private void loadMyInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot ds : datasnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("Phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();
                            myLatiitude= Double.valueOf(""+ds.child("latitude").getValue());
                            myLongitude  = Double.valueOf(""+ds.child("longitude").getValue());



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadShopDetail() {




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                            String name = ""+datasnapshot.child("name").getValue();


                             shopName = ""+datasnapshot.child("shopName").getValue();
                            shopEmail = ""+datasnapshot.child("email").getValue();
                            shopPhone = ""+datasnapshot.child("Phone").getValue();
                            ShopLatiitude= Double.valueOf(""+datasnapshot.child("latitude").getValue());
                            ShopLongitude  = Double.valueOf(""+datasnapshot.child("longitude").getValue());
                            String shopeAddressTV = ""+datasnapshot.child("address").getValue();
                            String deliveryFee = ""+datasnapshot.child("deliveryFees").getValue();
                            String profileImage = ""+datasnapshot.child("profileImage").getValue();
                            String shopOpen = ""+datasnapshot.child("shopOpen").getValue();

                           shopnameTV.setText(shopName);
                           shopemailTV.setText(shopEmail);
                           shopdeliveyfeeTV.setText(deliveryFee);
                           shopeAddress.setText(shopeAddressTV);
                           shopphoneTV.setText(shopPhone);

                           if (shopOpen.equals("true")){
                               shopopencloseTV.setText("Open");
                           }else {
                               shopopencloseTV.setText("Close");
                           }


//                            try {
//
//                                Picasso.get().load(profileImage).placeholder(R.drawable.baseline_store_24).into(R.drawable.baseline_store_24);
//
//                            }catch (Exception e){
//
//                                shopTV.setImageResource(R.drawable.baseline_store_24);
//
//                            }

                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadShopProducts() {

        //now initialize list
        productsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(shopUid).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        //clear list before show list

                        productsList.clear();

                       for (DataSnapshot ds : datasnapshot.getChildren()){
                           ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                           productsList.add(modelProduct);
                       }

                       //now setup Adaptor



                        adaptorUserShopProduct= new AdaptorUserShopProduct(getContext(),productsList);

                       //set adaptor
                       productRv.setAdapter(adaptorUserShopProduct);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAPS) {
            // Handle the result of the activity
            // For example, you can show a message to the user
            Toast.makeText(getActivity(), "Map activity finished", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadBannerImage() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).child("BannerImage").addValueEventListener(new ValueEventListener() {
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



}