package com.example.flashcart.profilePage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.LoginActivity;
import com.example.flashcart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.List;
import java.util.Locale;

public class ProfileActivityUser extends AppCompatActivity  implements LocationListener{

    ImageButton userbackbtn, usermylocation;
    ImageView sellerprofile;

    EditText emailET,pass,nameET,phoneET,countryET,stateET,cityET,addressET;
    Button userupdatebtn;

    SwitchCompat shopopenswitch;

    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] locationPermissions;
    private String[] cameraPermission;
    private String[] storagePermission;

    private double latitude = 0.0;
    private double longitude = 0.0;

    private LocationManager locationManager;
    private Uri image_uri;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileuser);


        userbackbtn = findViewById(R.id.loginbackbtn);
        usermylocation = findViewById(R.id.loginlocation);
        sellerprofile = findViewById(R.id.profilepic);
        emailET = findViewById(R.id.loginEmail);
        nameET = findViewById(R.id.loginName);
        phoneET = findViewById(R.id.loginPhone);
        countryET = findViewById(R.id.logincountry);
        stateET = findViewById(R.id.loginstate);
        cityET = findViewById(R.id.logincity);
        addressET = findViewById(R.id.loginaddress);

        //initializing all object

        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait while we are changing your detail on our end ....");
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseAuth = FirebaseAuth.getInstance();
        CheckUser();


        userupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputdata();

            }
        });



        sellerprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImagePickDialog();
            }
        });

        usermylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //detect location

                if(!checkLocationPermission()){
                    detectLocation();
                }else{
                    requestLocationPermission();
                }

            }
        });



    }


    String inputname,inputemail,inputpass,inputphone,inputshopname,inputcountry,inputstate,inputcity,inputaddress,inputfees;
    private void inputdata() {

        inputname = nameET.getText().toString().trim();
        inputemail = emailET.getText().toString().trim();
        inputpass = pass.getText().toString().trim();
        inputphone = phoneET.getText().toString().trim();
        inputcountry = countryET.getText().toString().trim();
        inputstate = stateET.getText().toString().trim();
        inputcity = cityET.getText().toString().trim();
        inputaddress = addressET.getText().toString().trim();

        updateProfile();


    }

    private void updateProfile() {
        progressDialog.setMessage("Updating your profile at our end Please wait.....");

        if(image_uri == null){
            //update data without image

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("email", "" +inputemail);
            hashMap.put("name","" + inputname);
            hashMap.put("Phone","" + inputphone);
            hashMap.put("country","" + inputcountry);
            hashMap.put("state","" + inputstate);
            hashMap.put("city","" + inputcity);
            hashMap.put("address", "" +inputaddress);
            hashMap.put("longitude", "" +longitude);
            hashMap.put("latitude", "" +latitude);


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //data updated
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivityUser.this, "Your data is been sucessfully updated", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed to update the database

                            Toast.makeText(ProfileActivityUser.this, "Fail to update the data", Toast.LENGTH_SHORT).show();

                        }
                    });


        }else{
            //update data with image
            String filePathAndName = "profiles_images/" + ""+ firebaseAuth.getUid();

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

                                //save actual data
                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("email", "" +inputemail);
                                hashMap.put("name","" + inputname);
                                hashMap.put("Phone","" + inputphone);
                                hashMap.put("country","" + inputcountry);
                                hashMap.put("state","" + inputstate);
                                hashMap.put("city","" + inputcity);
                                hashMap.put("address", "" +inputaddress);
                                hashMap.put("longitude", "" +longitude);
                                hashMap.put("latitude", "" +latitude);
                                hashMap.put("profileImage", ""+downloadImageUri); //url of upoaded image

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //data updated
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivityUser.this, "Your data is been sucessfully updated", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed to update the database

                                                Toast.makeText(ProfileActivityUser.this, "Fail to update the data", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(ProfileActivityUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }

    private void CheckUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }else{
            loadMyInfo();
        }

    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot ds : datasnapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            String address = ""+ds.child("address").getValue();
                            String city = ""+ds.child("city").getValue();
                            String state = ""+ds.child("state").getValue();
                            String country = ""+ds.child("country").getValue();
                            String email = ""+ds.child("email").getValue();
                            String longitude = ""+ds.child("longitude").getValue();
                            String latitude = ""+ds.child("latitude").getValue();
                            String name = ""+ds.child("name").getValue();
                            String online = ""+ds.child("Online").getValue();
                            String phone = ""+ds.child("Phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String timeStamp = ""+ds.child("timestamp").getValue();
                            String Uid = ""+ds.child("uid").getValue();

                            nameET.setText(name);
                            phoneET.setText(phone);
                            countryET.setText(country);
                            stateET.setText(state);
                            cityET.setText(city);
                            addressET.setText(address);
                            emailET.setText(email);

                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.baseline_person_24).into(sellerprofile);
                            }catch (Exception e){
                                sellerprofile.setImageResource(R.drawable.baseline_person_24);
                            }





                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void ShowImagePickDialog(){
        String[] option = {"Camera","Gallery"};

        //show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


    private boolean checkStoragePermission(){
        boolean result  = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermission(){
        boolean result  = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestcameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
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

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);


    }


    private void detectLocation() {

        Toast.makeText(this,"Please wait while location is being fetched .....",Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

    }

    private void findAddress(){

        //here we will find address country state and city

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());


        try {

            addresses = geocoder.getFromLocation(latitude,longitude,1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            //now we will set the address to editbox

            countryET.setText(country);
            stateET.setText(state);
            cityET.setText(city);
            addressET.setText(address);




        }catch (Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }




    private boolean checkLocationPermission() {

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,locationPermissions,LOCATION_REQUEST_CODE);

    }





    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        findAddress();
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
        Toast.makeText(this,"Please enable Loaction service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean loacationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(loacationAccepted){

                        //permission given

                    }else{

                        //permission not given
                        Toast.makeText(this,"Loaction Permission is nessasory before procedding",Toast.LENGTH_SHORT).show();

                    }


                }
            }
            break;

            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if(cameraAccepted && storageAccepted){

                        //permission given
                        pickFromCamera();

                    }else{

                        //permission not given
                        Toast.makeText(this,"Camera Permission is nessasory before procedding",Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(this,"Storage Permission is nessasory before procedding",Toast.LENGTH_SHORT).show();

                    }


                }
            }
            break;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode ==IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();

                //setting image to image box

                sellerprofile.setImageURI(image_uri);
            }else if(requestCode ==IMAGE_PICK_CAMERA_CODE){


                //setting image to image box

                sellerprofile.setImageURI(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

}