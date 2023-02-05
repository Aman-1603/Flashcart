package com.example.flashcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaCodec;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_accpassActivity extends AppCompatActivity {

    EditText recoveremail;
    Button recoverbtn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_accpass);

        //removing app bar

        recoveremail = findViewById(R.id.forgotEmail);
        recoverbtn = findViewById(R.id.forgotbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        recoverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPass();
            }
        });

    }

    private String email;
    private void recoverPass(){
         email = recoveremail.getText().toString().trim();
         if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
             Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
             return;
         }

         progressDialog.setMessage("Sending Instruction to reset password......");
         progressDialog.show();

         firebaseAuth.sendPasswordResetEmail(email)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                         //instruction send

                         progressDialog.dismiss();
                         Toast.makeText(Forgot_accpassActivity.this, "Password Instruction send to you email", Toast.LENGTH_SHORT).show();

                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         //failed sending Instructions
                         progressDialog.dismiss();
                         Toast.makeText(Forgot_accpassActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });

    }

}