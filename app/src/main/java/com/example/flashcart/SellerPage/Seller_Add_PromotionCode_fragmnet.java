package com.example.flashcart.SellerPage;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.flashcart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;


public class Seller_Add_PromotionCode_fragmnet extends Fragment {

    EditText promocodeEdt,promodescEdt,promopriceEdt,promoMinOrderEdt,promoexpriedateedt;
    Button addpromobtn;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;



    public Seller_Add_PromotionCode_fragmnet() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller__add__promotion_code_fragmnet, container, false);

        promocodeEdt = view.findViewById(R.id.promocodeEdt);
        promodescEdt = view.findViewById(R.id.promodescEdt);
        promopriceEdt = view.findViewById(R.id.promopriceEdt);
        promoMinOrderEdt = view.findViewById(R.id.promoMinOrderEdt);
        promoexpriedateedt = view.findViewById(R.id.promoexpriedateedt);
        addpromobtn = view.findViewById(R.id.addpromobtn);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);




        promoexpriedateedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datepickdialog();

            }
        });


        addpromobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputData();

            }
        });




        return view;
    }

    private void datepickdialog() {

        Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                DecimalFormat mFormat = new DecimalFormat("00");
                String pDay = mFormat.format(dayOfMonth);
                String pMonth = mFormat.format(month);
                String pYear = ""+year;
                String pDate = pDay + "/"+ pMonth + "/" + pYear;

                promoexpriedateedt.setText(pDate);


            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();

        //disable past selection date on calendar

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

    }


    private String promodescription,promocode,promoprice,minimumorderprice,promoexpiredate;
    private void inputData(){


        promocode = promocodeEdt.getText().toString().trim();
        promodescription = promodescEdt.getText().toString().trim();
        promoprice = promopriceEdt.getText().toString().trim();
        minimumorderprice = promoMinOrderEdt.getText().toString().trim();
        promoexpiredate = promoexpriedateedt.getText().toString().trim();



        if(TextUtils.isEmpty(promocode)){
            Toast.makeText(getContext(), "Please Enter PromoCode", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(promodescription)){
            Toast.makeText(getContext(), "Please Enter PromoCode", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(promoprice)){
            Toast.makeText(getContext(), "Please Enter PromoCode Price", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(minimumorderprice)){
            Toast.makeText(getContext(), "Please Enter Minimum OrderPrice", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(promoexpiredate)){
            Toast.makeText(getContext(), "Please Enter PromoCode Expire Date", Toast.LENGTH_SHORT).show();
            return;
        }




        pushDataToFirebase();

    }

    private void pushDataToFirebase() {


        progressDialog.setMessage("Adding Promocode Please Wait....");
        progressDialog.show();


        String timestamp = ""+System.currentTimeMillis();
        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("id", "" + timestamp);
        hashMap.put("timestamp", "" + timestamp);
        hashMap.put("PromoDescription", "" + promodescription);
        hashMap.put("PromoCode", "" + promocode);
        hashMap.put("PromoPrice", "" + promoprice);
        hashMap.put("MinOrderPrice", "" + minimumorderprice);
        hashMap.put("ExpireDate", "" + promoexpiredate);



//init db reference Users > Current User > Promotions > PromoID > Promo Data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users");

        ref.child(firebaseAuth.getUid()).child("PromotionCodes").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Promotion Code Added SuccessFully", Toast.LENGTH_SHORT).show();

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