package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.UriCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;

public class user_profile extends AppCompatActivity {
    private ImageView updateimg;
    private EditText updatename, updateemail, updatephonenumber;
    private Button updatebtn;
    private String name, email, phonenumber, img;
    private static final String Customers = "customer";
    //    private final int REQ=1;
//    private Bitmap bitmap=null;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference reference;
//    private String downloadUrl,uniquekey,category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phonenumber = getIntent().getStringExtra("phonenumber");
        img = getIntent().getStringExtra("image");

        updatename = findViewById(R.id.updatename);
        updateemail = findViewById(R.id.updateemail);
        updatephonenumber = findViewById(R.id.updatephonenumber);
        // updatebtn=findViewById(R.id.updatebtn);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Customers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(email)) {
                        updatename.setText(ds.child("name").getValue(String.class));
                        updateemail.setText(ds.child("email").getValue(String.class));
                        updatephonenumber.setText(ds.child("phonenumber").getValue(String.class));

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}