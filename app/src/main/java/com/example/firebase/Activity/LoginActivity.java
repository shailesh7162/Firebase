package com.example.firebase.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static Activity activity;
    EditText email_edt,password_edt;
    Button login_btn;
    TextView register_text;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_edt=findViewById(R.id.email_edt);
        password_edt=findViewById(R.id.password_edt);
        login_btn=findViewById(R.id.login_btn);
        register_text=findViewById(R.id.register_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        activity = this;
        mAuth = FirebaseAuth.getInstance();

        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email_edt.getText().toString().isEmpty() && !password_edt.getText().toString().isEmpty()) {
                    if (password_edt.getText().toString().length() >= 6) {
                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setTitle("Please Wait");
                        dialog.setMessage("Signing in...");
                        dialog.setCancelable(false);
                        dialog.show();
                        signIn();
                    } else {
                        password_edt.setError("Enter 6 or more Characters");
                    }
                } else {
                    if (email_edt.getText().toString().isEmpty()){
                        email_edt.setError("Enter Email");
                    }
                    if (password_edt.getText().toString().isEmpty()){
                        password_edt.setError("Enter Password");
                    }
                }
            }
        });
    }

    private void signIn() {

        mAuth.signInWithEmailAndPassword(email_edt.getText().toString(), password_edt.getText().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            StartActivity.editor.putString("method", "email");
                            StartActivity.editor.commit();
                            Toast.makeText(LoginActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            StartActivity.activity.finish();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}