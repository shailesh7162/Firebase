package com.example.firebase.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.firebase.Adapter.Product_Adapter;
import com.example.firebase.R;
import com.example.firebase.Modal.product_data;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Show_productActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference mbase;
    Product_Adapter adapter;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        recyclerView=findViewById(R.id.show_recycle);


        mbase = FirebaseDatabase.getInstance().getReference();

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("ProductData")
                .limitToLast(50);

        FirebaseRecyclerOptions<product_data> options
                = new FirebaseRecyclerOptions.Builder<product_data>()
                .setQuery(query, product_data.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new Product_Adapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}