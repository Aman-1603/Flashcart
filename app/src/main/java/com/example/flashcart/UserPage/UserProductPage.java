package com.example.flashcart.UserPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class UserProductPage extends Fragment {


    TextView productTitleTV,productTitle1TV,ProductPriceTV,product_descriptionTV,addressTV,cityTV,StateTV,CountryTV,PhoneTV;

    ImageView productimage;
    TextView productUid;


    String ItemUid;
    String ShopUid;

    private FirebaseAuth firebaseAuth;

    public UserProductPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_product_page, container, false);



        productTitleTV = view.findViewById(R.id.product_title);
        productTitle1TV = view.findViewById(R.id.productTitle1TV);
        ProductPriceTV = view.findViewById(R.id.ProductPriceTV);
        product_descriptionTV = view.findViewById(R.id.product_descriptionTV);
        addressTV = view.findViewById(R.id.addressTV);
        cityTV = view.findViewById(R.id.cityTV);
        StateTV = view.findViewById(R.id.StateTV);
        CountryTV = view.findViewById(R.id.CountryTV);
        PhoneTV = view.findViewById(R.id.PhoneTV);
        productUid = view.findViewById(R.id.productUid);
        productimage = view.findViewById(R.id.product_image);


        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            ItemUid = bundle.getString("ItemUid");
            ShopUid = bundle.getString("ShopUid");
        }


        loadmyInfo();
        loadproductDetail();



        return view;
    }

    private void loadmyInfo() {

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
                            String state = ""+ds.child("state").getValue();
                            String country = ""+ds.child("country").getValue();
                            String address = ""+ds.child("address").getValue();

//                            myLatiitude= Double.valueOf(""+ds.child("latitude").getValue());
//                            myLongitude  = Double.valueOf(""+ds.child("longitude").getValue());



                            PhoneTV.setText(phone);
                            cityTV.setText(city);
                            StateTV.setText(state);
                            CountryTV.setText(country);
                            addressTV.setText(address);

                            productUid.setText(ItemUid);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadproductDetail() {



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(ShopUid).child("Products");
        ref.orderByChild("ItemUid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot ds : datasnapshot.getChildren()){
                    String itemUid = ds.getKey(); // get the unique ID of the current product

                    // only update the views if the current product's ID matches the desired ID
                    if(itemUid.equals(ItemUid)){
                        String title = ds.child("ProductTitle").getValue(String.class);
                        String price = ds.child("ProductPrice").getValue(String.class);
                        String description = ds.child("ProductDescription").getValue(String.class);
                        String image = ds.child("ProductIcon").getValue(String.class);

                        // update the views for the current product
                        productTitleTV.setText(title);
                        productTitle1TV.setText(title);
                        ProductPriceTV.setText(price);
                        product_descriptionTV.setText(description);
                        Picasso.get().load(image).into(productimage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}