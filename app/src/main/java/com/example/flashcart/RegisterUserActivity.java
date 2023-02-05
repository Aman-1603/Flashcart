package com.example.flashcart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import  android.Manifest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RegisterUserActivity extends AppCompatActivity implements LocationListener {


    ImageButton loginbackbtn, loginmylocation;
    ImageView profile;

    EditText email,pass,name,phone,countryET,stateET,cityET,addressET;
    Button registerbtn;
    TextView registerseller;




    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;


    private String[] locationPermissions;
    private String[] cameraPermission;
    private String[] storagePermission;
    private LocationManager locationManager;
    private double longitude,latilude;
    private Uri image_uri;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //removing appbar

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginbackbtn = findViewById(R.id.loginbackbtn);
        loginmylocation = findViewById(R.id.loginlocation);
        profile = findViewById(R.id.profilepic);
        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginPass);
        name = findViewById(R.id.loginName);
        phone = findViewById(R.id.loginPhone);
        countryET = findViewById(R.id.logincountry);
        stateET = findViewById(R.id.loginstate);
        cityET = findViewById(R.id.logincity);
        addressET = findViewById(R.id.loginaddress);
        registerbtn = findViewById(R.id.registerbtn);
        registerseller = findViewById(R.id.areyouseller);



        //initializing perssmison location

        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //initializing firebase

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait a movenment ....");
        progressDialog.setCanceledOnTouchOutside(false);



        //back button

        loginbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // are you seller

        registerseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUserActivity.this,RegisterSellerActivity.class));
            }
        });

        //detecteing gps

        loginmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here detecting location
                if(checkLocationPermission()){
                    detectLocation();
                }else{
                    requestLocationPermission();
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                showImagePickDialoge();
            }
        });


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //register the user here
                inputdata();

            }
        });



    }


    String inputname,inputemail,inputpass,inputphone,inputshopname,inputcountry,inputstate,inputcity,inputaddress,inputfees;
    private void inputdata(){
        inputname = name.getText().toString().trim();
        inputemail = email.getText().toString().trim();
        inputpass = pass.getText().toString().trim();
        inputphone = phone.getText().toString().trim();
        inputcountry = countryET.getText().toString().trim();
        inputstate = stateET.getText().toString().trim();
        inputcity = cityET.getText().toString().trim();
        inputaddress = addressET.getText().toString().trim();

        if(TextUtils.isEmpty(inputname)){
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(inputemail)){
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(inputpass)){
            Toast.makeText(this, "Please Enter Your Pass", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(inputphone)){
            Toast.makeText(this, "Please Enter Your Phone", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(inputcountry)){
            Toast.makeText(this, "Please Enter Your Country", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(inputstate)){
            Toast.makeText(this, "Please Enter Your State", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(inputcity)){
            Toast.makeText(this, "Please Enter Your City", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(inputaddress)){
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
            return;
        }


        if(latilude == 0.0 || longitude == 0.0){
            Toast.makeText(this, "Please Click on Gps Button for one Tme", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()){
            Toast.makeText(this, "Please Enter Your Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(inputpass.length() <6){
            Toast.makeText(this, "Password Should not be less than 6 digit or character", Toast.LENGTH_SHORT).show();
            return;
        }


        //if all condition exist than we will call create acount function for further process

        createAccount();



    }


    private void createAccount(){
        progressDialog.setMessage("Please Wait while we are creating your Account on our server");
        progressDialog.show();

        //create a account

        firebaseAuth.createUserWithEmailAndPassword(inputemail,inputpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //account created
                        saveFirebaseData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //account creation fail
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void saveFirebaseData(){
        progressDialog.setMessage("Saving Account");

        String timestamp = ""+System.currentTimeMillis();

        if(image_uri == null){
            //then even without image we will save the data to firebase

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("uid", "" +firebaseAuth.getUid());
            hashMap.put("email", "" +inputemail);
            hashMap.put("name","" + inputname);
            hashMap.put("Phone","" + inputphone);
            hashMap.put("country","" + inputcountry);
            hashMap.put("state","" + inputstate);
            hashMap.put("city","" + inputcity);
            hashMap.put("address", "" +inputaddress);
            hashMap.put("longitude", "" +longitude);
            hashMap.put("latitude", "" +latilude);
            hashMap.put("timestamp", "" +timestamp);
            hashMap.put("accountType", "User");
            hashMap.put("Online",  "true");
            hashMap.put("profileImage", "");

            //now we will save all this detail in database

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //update database
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUserActivity.this,MainUserActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed to update the database
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUserActivity.this,MainUserActivity.class));
                            finish();
                        }
                    });


        }else{
            //we will save information with image

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

                                hashMap.put("uid", "" +firebaseAuth.getUid());
                                hashMap.put("email", "" +inputemail);
                                hashMap.put("name","" + inputname);
                                hashMap.put("Phone","" + inputphone);
                                hashMap.put("country","" + inputcountry);
                                hashMap.put("state","" + inputstate);
                                hashMap.put("city","" + inputcity);
                                hashMap.put("address", "" +inputaddress);
                                hashMap.put("longitude", "" +longitude);
                                hashMap.put("latitude", "" +latilude);
                                hashMap.put("timestamp", "" +timestamp);
                                hashMap.put("accountType", "User");
                                hashMap.put("Online",  "true");
                                hashMap.put("profileImage", ""+downloadImageUri); //url of upoaded image

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //fail to get image url
                            progressDialog.dismiss();
                            Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }





    private void showImagePickDialoge(){
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

            addresses = geocoder.getFromLocation(latilude,longitude,1);
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


    private boolean checkLocationPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this,locationPermissions,LOCATION_REQUEST_CODE);
    }



    // all methods of "implement location listener"


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latilude = location.getLatitude();
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



    // "Location Listener up to these"



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

                profile.setImageURI(image_uri);
            }else if(requestCode ==IMAGE_PICK_CAMERA_CODE){


                //setting image to image box

                profile.setImageURI(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}