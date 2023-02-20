package com.example.flashcart.SellerPage;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flashcart.R;
import com.example.flashcart.RegisterSellerActivity;
import com.example.flashcart.categorylist.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SellerAddProduct_fragment extends Fragment {


    EditText titleproductEt,descriptionEt,categoryEt,quantityEt,priceEt,discountEt,discountnoteEt;
    ImageView productIconTv;
    Button addproductbtn;
    SwitchCompat switchdisacount;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri image_uri;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    public SellerAddProduct_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_seller_add_product_fragment, container, false);

        titleproductEt = view.findViewById(R.id.titleET);
        descriptionEt = view.findViewById(R.id.descriptionEt);
        categoryEt = view.findViewById(R.id.categoryEt);
        quantityEt = view.findViewById(R.id.QuantityEt);
        priceEt = view.findViewById(R.id.priceEt);
        discountEt = view.findViewById(R.id.discountPriceEt);
        discountnoteEt = view.findViewById(R.id.discountNoteEt);
        switchdisacount = view.findViewById(R.id.discountswitch);
        productIconTv = view.findViewById(R.id.productImgEt);
        addproductbtn = view.findViewById(R.id.addproductbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait a movenment ....");
        progressDialog.setCanceledOnTouchOutside(false);



        //initialize permission array

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //if discount switch is checked then we will show discount box
        switchdisacount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    //if checked than show these box
                    discountEt.setVisibility(View.VISIBLE);
                    discountnoteEt.setVisibility(View.VISIBLE);
                }else{
                    //if not checked then we will hide these two edittext
                    discountEt.setVisibility(View.GONE);
                    discountnoteEt.setVisibility(View.GONE);

                }
            }
        });

        productIconTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImagePickDialog();
            }
        });

        categoryEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDialog();
            }
        });

        return view;
    }

    private void CategoryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Product Category")
                .setItems(Constants.productcategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String category = Constants.productcategories[which];

                        //now set picked category
                        categoryEt.setText(category);
                    }
                }).show();


        addproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

                //flow of data
                //1->input data
                //2->validate data
                //3->store data to database

                inputData();
            }
        });

    }


    String ProductTitle,ProductDescription,ProductCategory,ProductQuantity,ProductOrignalPrice,ProductDiscount,ProductDiscountNote;
    boolean discountAvailable = false;
    private void inputData() {
        //1 inputdata
        ProductTitle = titleproductEt.getText().toString().trim();
        ProductDescription = descriptionEt.getText().toString().trim();
        ProductCategory = categoryEt.getText().toString().trim();
        ProductQuantity = quantityEt.getText().toString().trim();
        ProductOrignalPrice = priceEt.getText().toString().trim();
        discountAvailable = switchdisacount.isChecked();

        if(TextUtils.isEmpty(ProductTitle)){
            Toast.makeText(getContext(), "Please Enter Product Title", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(ProductDescription)){
            Toast.makeText(getContext(), "Please Enter Product Description", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(ProductCategory)){
            Toast.makeText(getContext(), "Please Enter Product Category", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(ProductQuantity)){
            Toast.makeText(getContext(), "Please Enter Product Quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(ProductOrignalPrice)){
            Toast.makeText(getContext(), "Please Enter Product Orignal Price", Toast.LENGTH_SHORT).show();
            return;
        }

        if(discountAvailable){
            ProductDiscount = discountEt.getText().toString().trim();
            ProductDiscountNote = discountnoteEt.getText().toString().trim();


            if(TextUtils.isEmpty(ProductDiscount)){
                Toast.makeText(getContext(), "Please Enter Product Discount Amount", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(ProductDiscountNote)){
                Toast.makeText(getContext(), "Please Enter Product Discount Note", Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            ProductDiscount = "0";
            ProductDiscountNote = "";
        }

        addProduct();



    }

    private void addProduct() {

        //3 add product
        progressDialog.setMessage("Adding Product");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        if(image_uri == null){
            //upload product without image
            HashMap<String, Object> hashMap = new HashMap<>();

            //setup data to upload

            hashMap.put("ProductId",""+timestamp);
            hashMap.put("ProductTitle",""+ProductTitle);
            hashMap.put("ProductDescription",""+ProductDescription);
            hashMap.put("ProductCategory",""+ProductCategory);
            hashMap.put("ProductQuantity",""+ProductQuantity);
            hashMap.put("ProductIcon",""); //no image
            hashMap.put("ProductPrice",""+ProductOrignalPrice);
            hashMap.put("ProductDiscountPrice",""+ProductDiscount);
            hashMap.put("ProductDiscountNote",""+ProductDiscountNote);
            hashMap.put("ProductDiscountAvailable",""+discountAvailable);
            hashMap.put("TimeStamp",""+timestamp);
            hashMap.put("uid",""+firebaseAuth.getUid());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Products").child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Products Added", Toast.LENGTH_SHORT).show();
                            
                            ClearData();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           progressDialog.dismiss();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });





        }else{


            //we will save information with image

            String filePathAndName = "product_images/" + ""+ timestamp;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //here we will try to get url for uploaded image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if(uriTask.isSuccessful()){

                                HashMap<String, Object> hashMap = new HashMap<>();

                                //setup data to upload

                                hashMap.put("ProductId",""+timestamp);
                                hashMap.put("ProductTitle",""+ProductTitle);
                                hashMap.put("ProductDescription",""+ProductDescription);
                                hashMap.put("ProductCategory",""+ProductCategory);
                                hashMap.put("ProductQuantity",""+ProductQuantity);
                                hashMap.put("ProductIcon",""+downloadImageUri); //no image
                                hashMap.put("ProductPrice",""+ProductOrignalPrice);
                                hashMap.put("ProductDiscountPrice",""+ProductDiscount);
                                hashMap.put("ProductDiscountNote",""+ProductDiscountNote);
                                hashMap.put("ProductDiscountAvailable",""+discountAvailable);
                                hashMap.put("TimeStamp",""+timestamp);
                                hashMap.put("uid",""+firebaseAuth.getUid());

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseAuth.getUid()).child("Products").child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Products Added", Toast.LENGTH_SHORT).show();
                                                ClearData();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //fail to get image url
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }



    }

    private void ClearData() {

        titleproductEt.setText("");
        descriptionEt.setText("");
        categoryEt.setText("");
        quantityEt.setText("");
        priceEt.setText("");
        discountEt.setText("");
        discountnoteEt.setText("");
        productIconTv.setImageResource(R.drawable.baseline_add_shopping_cart_24);
        image_uri = null;


    }

    private void ShowImagePickDialog(){
        String[] option = {"Camera","Gallery"};

        //show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Your Image")
                .setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which == 0){
                            // then camera clicked

                            if(checkCameraPermission()){
                                //then camera permission allowed

                                pickFromCamera();


                            }else{
                                //camera permission not allowed, request

                                requestcameraPermission();

                            }


                        }else{
                            //otherwise gallery clicked

                            if(checkStoragePermission()){
                                //storage permission allowed

                                pickedFromGallery();

                            }else{

                                // storage permission not allowed, requesting
                                requestStoragePermission();

                            }

                        }

                    }
                })
                .show();
    }


    private void pickedFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Descritption");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);


    }

    private boolean checkStoragePermission(){
        boolean result  = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(getActivity(),storagePermission,STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermission(){
        boolean result  = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestcameraPermission(){
        ActivityCompat.requestPermissions(getActivity(),cameraPermission,CAMERA_REQUEST_CODE);
    }


    //handle permission request

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if(cameraAccepted && storageAccepted){

                        //permission given
                        pickFromCamera();

                    }else{

                        //permission not given
                        Toast.makeText(getContext(),"Camera Permission is nessasory before procedding",Toast.LENGTH_SHORT).show();

                    }


                }
            }
            break;


            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if(storageAccepted){

                        //permission given

                        pickedFromGallery();


                    }else{

                        //permission not given
                        Toast.makeText(getContext(),"Storage Permission is nessasory before procedding",Toast.LENGTH_SHORT).show();

                    }


                }
            }
            break;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode ==IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();

                //setting image to image box

                productIconTv.setImageURI(image_uri);
            }else if(requestCode ==IMAGE_PICK_CAMERA_CODE){


                //setting image to image box

                productIconTv.setImageURI(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }



}