package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText editemail,editpassword,editnumber,editnumpassword;
    Button login,register,numlogin;
    TextView google;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editemail=findViewById(R.id.edit_email);
        editpassword=findViewById(R.id.edit_password);
        editnumber=findViewById(R.id.edit_number);
        editnumpassword=findViewById(R.id.edit_numpassword);
        login=findViewById(R.id.login_btn);
        register=findViewById(R.id.register_btn);
        numlogin=findViewById(R.id.loginnum_btn);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String email,password;
                email=editemail.getText().toString();
                password=editpassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TTT", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("EEE", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                    //  updateUI(null);
                                }
                            }
                        });
            }

        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email=editemail.getText().toString();
                password=editpassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TTT", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("EEE", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                   // updateUI(null);
                                }
                            }
                        });
            }
        });
    }


}