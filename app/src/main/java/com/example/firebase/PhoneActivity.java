package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ProgressDialog dialog1;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    EditText num_edt,num_password;
    Button login_num,getotp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        num_edt=findViewById(R.id.num_edt);
        num_password=findViewById(R.id.num_password);
        login_num=findViewById(R.id.login_num);
        getotp=findViewById(R.id.getotp);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mAuth = FirebaseAuth.getInstance();
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!num_edt.getText().toString().isEmpty()){
                    if (num_edt.getText().toString().length() == 10){
                        dialog1 = new ProgressDialog(PhoneActivity.this);
                        dialog1.setTitle("Please Wait");
                        dialog1.setMessage("Sending OTP...");
                        dialog1.setCancelable(false);
                        dialog1.show();
                        phoneSignIn();
                    } else {
                        num_edt.setError("Enter Proper Number");
                    }
                } else {
                    num_edt.setError("Enter Number");
                }
            }
        });
        login_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!getotp.getText().toString().isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, getotp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                } else {
                    getotp.setError("Enter OTP");
                }
            }
        });
    }
    private void phoneSignIn() {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
                dialog1.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                dialog1.dismiss();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                login_num.setEnabled(true);
                dialog1.dismiss();
            }
        };


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + num_edt.getText().toString())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        ProgressDialog dialog = new ProgressDialog(PhoneActivity.this);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Verifying OTP...");
        dialog.setCancelable(false);
        dialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            StartActivity.editor.putString("method", "phone");
                            StartActivity.editor.commit();
                            Toast.makeText(PhoneActivity.this, "User Sign In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                            StartActivity.activity.finish();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(PhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}