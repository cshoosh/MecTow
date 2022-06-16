package com.example.mectow;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mectow.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;

import static com.example.mectow.user_MapsActivity.customerrequestuid;

public class Activity_rate extends Activity {
    String discountname ;
    TextView Cname, Mname, catagory, charges;
    RatingBar ratingBar;
    DecimalFormat format;
    FirebaseAuth auth;
    String id;
    DatabaseReference reference;
    double rate, f_rate;
    String servicetype;
    String mid = "";
    Button ratingbtn_click;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        //initialize
        Cname = findViewById(R.id.name_text);
        Mname = findViewById(R.id.name_rate_field);
        catagory = findViewById(R.id.category_field);
        charges = findViewById(R.id.user_rating);
        ratingBar = findViewById(R.id.simpleRatingBar);
        ratingbtn_click=findViewById(R.id.ratingbtn);
        auth = FirebaseAuth.getInstance();
        servicetype = user_MapsActivity.servicetype;
        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);


        reference = FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                catagory.setText(dataSnapshot.child("Category").getValue().toString());
                if (servicetype == "mechanic") {
                    charges.setText(dataSnapshot.child("charges").getValue().toString());
                } else if (servicetype == "cartow") {
                    charges.setText(dataSnapshot.child("totalcharges").getValue().toString());
                }

                mid = dataSnapshot.child("mid").getValue().toString();
                reference = FirebaseDatabase.getInstance().getReference("Mechanic").child(mid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Cname.setText(dataSnapshot.child("name").getValue().toString());
                        Mname.setText(dataSnapshot.child("name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                reference = FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        id = dataSnapshot.child("mid").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                f_rate = (rate + v) / 2;   //For mean rating

                format = new DecimalFormat("0.0");      //For 1 decimal place rating i.e (4.5)

                if (v <= 3) {
                    Intent intent = new Intent(getApplicationContext(),Complain_activity.class);
                    intent.putExtra("id" , customerrequestuid);
                    intent.putExtra("rating",format.format(f_rate));
                    startActivity(intent);
                    finishAffinity();
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
                    HashMap<String, Object> rate = new HashMap<>();
                    rate.put("rating", format.format(f_rate));
                    reference.updateChildren(rate);

                    Toast.makeText(getApplicationContext(), "Thank you for the feedback to MecTow", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Home_Navigation.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });

}


    }
