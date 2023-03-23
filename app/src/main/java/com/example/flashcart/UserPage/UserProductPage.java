package com.example.flashcart.UserPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.flashcart.Model.ModelProduct;
import com.example.flashcart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;


public class UserProductPage extends Fragment {


    TextView productTitleTV,productTitle1TV,ProductPriceTV,product_descriptionTV,addressTV,cityTV,StateTV,CountryTV,PhoneTV;

    ImageView productimage;
    TextView productUid;

    String ProductIcon,ProductTitle,ProductPrice,ProductDescription,ProductCategory,ProductDiscountPrice,ProductDiscountNote,ProductDiscountAvailable,ProductQuantity;

    Button addtoCart;

    String ItemUid;
    String ShopUid;

    private ProgressDialog progressDialog;

    private Uri image_uri;

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
        addtoCart = view.findViewById(R.id.add_to_cart_button);


        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            // Retrieve the data using the key-value pairs
            ItemUid = bundle.getString("ItemUid");
            ShopUid = bundle.getString("ShopUid");
        }



        loadmyInfo();
        loadproductDetail();


        ModelProduct modelProduct = new ModelProduct();

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCart(modelProduct);
            }
        });




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

                            productUid.setText(ShopUid);

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
                         ProductTitle = ds.child("ProductTitle").getValue(String.class);
                         ProductPrice = ds.child("ProductPrice").getValue(String.class);
                         ProductDescription = ds.child("ProductDescription").getValue(String.class);
                         ProductIcon = ds.child("ProductIcon").getValue(String.class);
                        ProductCategory = ds.child("ProductCategory").getValue(String.class);
                        ProductDiscountPrice = ds.child("ProductDiscountPrice").getValue(String.class);
                        ProductDiscountNote = ds.child("ProductDiscountNote").getValue(String.class);
                        ProductDiscountAvailable = ds.child("ProductDiscountAvailable").getValue(String.class);
                        ProductQuantity = ds.child("ProductQuantity").getValue(String.class);





                        // update the views for the current product
                        productTitleTV.setText(ProductTitle);
                        productTitle1TV.setText(ProductTitle);
                        ProductPriceTV.setText("₹"+ProductPrice);
                        product_descriptionTV.setText(ProductDescription);
                        Picasso.get().load(ProductIcon).into(productimage);

                        image_uri = Uri.parse(ProductIcon);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


      double cost = 0;
     double finalcost = 0;
     int quantity1 = 0;

    private void addtoCart(ModelProduct modelProduct) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_quantity, null);

        LinearLayout innerlayout = view.findViewById(R.id.innerLayout);

        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView Quantity = view.findViewById(R.id.showquantityTv);
        TextView descriptionTV = view.findViewById(R.id.descriptionTv);
        TextView orignalPriceTv = view.findViewById(R.id.orignalPriceTv);
        TextView discountPriceTv = view.findViewById(R.id.discountPriceTv);
        TextView finalPriceTv = view.findViewById(R.id.finalPriceTv);
        TextView showquantityTv = view.findViewById(R.id.showquantityTv);
        ImageButton minusbutton = view.findViewById(R.id.minusbutton);
        ImageButton plusbutton = view.findViewById(R.id.plusbutton);
        CircularImageView producticonTv = view.findViewById(R.id.producticonTv);
        Button AddToCart = view.findViewById(R.id.AddToCart);





        String price;

        if (ProductDiscountAvailable.equals("true")) {

            //then product have discount

            price = ProductDiscountPrice;



        } else {

            price = ProductPrice;

        }





        cost = Double.parseDouble(price.replaceAll("$", ""));
        finalcost = Double.parseDouble(price.replaceAll("$", ""));
        quantity1 = 1;


        //setting up alert builder

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);



        try {

            Picasso.get().load(ProductIcon).into(producticonTv);

        }catch (Exception e){
            producticonTv.setImageResource(R.drawable.baseline_add_shopping_black_cart_24);
        }


//        description

        titleTv.setText(ProductTitle);
        descriptionTV.setText(ProductDescription);
        showquantityTv.setText(""+quantity1);
        orignalPriceTv.setText(ProductPrice);
        discountPriceTv.setText(ProductDiscountPrice);
        finalPriceTv.setText(""+finalcost);



        AlertDialog dialog = builder.create();
        dialog.show();


        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalcost = finalcost + cost;
                quantity1++;

                finalPriceTv.setText(""+finalcost);
                showquantityTv.setText(""+quantity1);
            }
        });

        minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (quantity1 > 1){
                    finalcost = finalcost - cost;
                    quantity1--;

                    finalPriceTv.setText(""+finalcost);
                    showquantityTv.setText(""+quantity1);


                }

            }
        });

        AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleTv.getText().toString().trim();
                String priceEach = orignalPriceTv.getText().toString().trim();
                String price = finalPriceTv.getText().toString().trim();
                String quantity = showquantityTv.getText().toString().trim();



                addToCart(ItemUid,title,priceEach,price,quantity);
                
                dialog.dismiss();



            }
        });

    }


    private void addToCart(String itemUid, String title, String priceEach, String price, String quantity) {

//        progressDialog.setMessage("Adding Product to cart please wait");
//        progressDialog.show();

        String timestamp = ""+itemUid;
//        image_uri = Uri.parse(ProductIcon);




                            //setup data to upload

                            HashMap<String, Object> cartItem = new HashMap<>();

                            cartItem.put("itemUid", itemUid);
                            cartItem.put("ShopUid",ShopUid);
                            cartItem.put("title", title);
                            cartItem.put("priceEach", priceEach);

                            cartItem.put("price", price);
                            cartItem.put("quantity", quantity);

                            cartItem.put("ProductIcon",ProductIcon); //no image
                            cartItem.put("uid", "" + firebaseAuth.getUid());


                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                            reference.child(firebaseAuth.getUid()).child("AddtoCart").child(timestamp).setValue(cartItem)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

//                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Products Added", Toast.LENGTH_SHORT).show();


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
//                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    }




        // Create a HashMap to store the cart item data







//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference cartRef = database.getReference("Users").child(firebaseAuth.getUid()).child("AddtoCart");

//    HashMap<String, Object> cartItem = new HashMap<>();
//        cartItem.put("itemUid", itemUid);
//                cartItem.put("ShopUid",ShopUid);
//                cartItem.put("title", title);
//                cartItem.put("priceEach", priceEach);
//
//                cartItem.put("price", price);
//                cartItem.put("quantity", quantity);
//
//                // Add the cart item data to the Firebase database
//                cartRef.push().setValue(cartItem);
