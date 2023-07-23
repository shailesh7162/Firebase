package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DrawerLayout drawer;
    NavigationView navigation;
    Toolbar toolbar;
    String method;
    EditText edt_name,edt_price,edt_des;
    Button addBtn;
    ImageView add_product_img;
    FirebaseStorage storage;
    private StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);

        mAuth = FirebaseAuth.getInstance();
        method = StartActivity.preferences.getString("method", "h");

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigation.setCheckedItem(R.id.nav_add_product);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                if (item.getItemId() == R.id.nav_add_product)
                {
                    Dialog dialog=new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.add_product);
                    add_product_img=dialog.findViewById(R.id.add_product_img);
                    edt_name=dialog.findViewById(R.id.edt_name);
                    edt_price=dialog.findViewById(R.id.edt_price);
                    edt_des=dialog.findViewById(R.id.edt_des);
                    addBtn=dialog.findViewById(R.id.addBtn);

                    storage = FirebaseStorage.getInstance();
                    storageReference = FirebaseStorage.getInstance().getReference();
                    String imageName = "Img" + new Random().nextInt(10000) + ".jpg";
                    StorageReference Storagebucket = storageReference.child(" Storagebucket/"+imageName);

                    // While the file names are the same, the references point to different files
                    Storagebucket.getName().equals( Storagebucket.getName());    // true
                    Storagebucket.getPath().equals( Storagebucket.getPath());    // false

                    add_product_img.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {

                            CropImage.activity()
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(MainActivity.this);
                        }
                    });

                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String name,price,dec;
                            if (edt_name.getText().toString().equals(""))
                            {
                                edt_name.setError("Field can't be empty",getResources().getDrawable(R.drawable.baseline_error_24));
                            } else if (edt_price.getText().toString().equals("")) {
                                edt_price.setError("Field can't be empty",getResources().getDrawable(R.drawable.baseline_error_24));
                            }
                            else if(edt_des.getText().toString().equals(""))
                            {
                                edt_des.setError("Field can't be empty",getResources().getDrawable(R.drawable.baseline_error_24));

                            }
                            else
                            {
                                name = edt_name.getText().toString();
                                price = edt_price.getText().toString();
                                dec = edt_des.getText().toString();

                                add_product_img.setDrawingCacheEnabled(true);
                                add_product_img.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) add_product_img.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();

                                UploadTask uploadTask = Storagebucket.putBytes(data);

                                uploadTask.addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if (!task.isSuccessful()) {
                                                    throw task.getException();
                                                }

                                                // Continue with the task to get the download URL
                                                return Storagebucket.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    Uri downloadUri = task.getResult();
                                                    String imgUrl= String.valueOf(downloadUri);
                                                    database = FirebaseDatabase.getInstance();
                                                    myRef = database.getReference("ProductData").push();
                                                    String id = myRef.getKey();
                                                    product_data product_data=new product_data(id,name,price,dec,imgUrl);
                                                    myRef.setValue(product_data);

                                                } else {
                                                }
                                            }
                                        });


                                    }
                                });

                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
                if(item.getItemId()==R.id.nav_show_product){
                    Intent intent=new Intent(MainActivity.this,Show_productActivity.class);
                    startActivity(intent);
                }
                 if (item.getItemId() == R.id.menu_logout){
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                    finish();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                add_product_img.setImageURI(resultUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}