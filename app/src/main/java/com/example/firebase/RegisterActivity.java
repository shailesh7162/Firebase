package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText edt_name,edt_email,edt_password;
    Button register_btn;
    private FirebaseAuth mAuth;

    TextView login_text;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edt_name=findViewById(R.id.edt_name);
        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        register_btn=findViewById(R.id.register_btn);
        login_text=findViewById(R.id.login_text);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mAuth = FirebaseAuth.getInstance();


        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (!edt_name.getText().toString().isEmpty() && !edt_email.getText().toString().isEmpty() && !edt_password.getText().toString().isEmpty()) {
                    if (edt_password.getText().toString().length() >= 6) {
                        dialog = new ProgressDialog(RegisterActivity.this);
                        dialog.setTitle("Please Wait");
                        dialog.setMessage("Registering User...");
                        dialog.setCancelable(false);
                        dialog.show();
                        registerUser();
                    } else {
                        edt_password.setError("Enter 6 or more Characters");
                    }
                } else {
                    if (edt_name.getText().toString().isEmpty()){
                        edt_name.setError("Enter Name");
                    }
                    if (edt_email.getText().toString().isEmpty()){
                        edt_email.setError("Enter Email");
                    }
                    if (edt_password.getText().toString().isEmpty()){
                        edt_password.setError("Enter Password");
                    }
                }
            }
        });
    }
    public void registerUser(){
        mAuth.createUserWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString())
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            StartActivity.editor.putString("method", "email");
                            StartActivity.editor.commit();
                            Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            LoginActivity.activity.finish();
                            StartActivity.activity.finish();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}