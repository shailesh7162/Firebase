package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText editname,editpassword,editnumber,editnumpassword;
    Button login,register,numlogin;
    TextView sing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editname=findViewById(R.id.edit_name);
        editpassword=findViewById(R.id.edit_password);
        editnumber=findViewById(R.id.edit_number);
        editnumpassword=findViewById(R.id.edit_numpassword);
        login=findViewById(R.id.login_btn);
        register=findViewById(R.id.register_btn);
        numlogin=findViewById(R.id.loginnum_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}