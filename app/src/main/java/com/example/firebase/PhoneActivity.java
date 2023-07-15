package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class PhoneActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProgressDialog dialog1;
    private String mVerificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        mAuth = FirebaseAuth.getInstance();
    }
}