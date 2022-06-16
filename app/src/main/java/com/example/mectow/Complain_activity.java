package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class Complain_activity extends AppCompatActivity {

    TextView complain1, complain2, complain3, complain4;
    EditText othdetail;
    String id, rate,mid;
    DecimalFormat format;
    DatabaseReference reference;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_activity);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        rate = intent.getStringExtra("rating");
        auth = FirebaseAuth.getInstance();


        complain1 = findViewById(R.id.complain1);
        complain2 = findViewById(R.id.complain2);
        complain3 = findViewById(R.id.complain3);
        complain4 = findViewById(R.id.complain4);
        othdetail = findViewById(R.id.otherdetail);

        reference = FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mid = dataSnapshot.child("mid").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });





        complain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(complain1.getCurrentTextColor() == getResources().getColor(R.color.cadetBlue)) {
                    complain1.setTextColor(getResources().getColor(R.color.colorAccent));
                    complain4.setTextColor(getResources().getColor(R.color.cadetBlue));
                    othdetail.setVisibility(View.INVISIBLE);
                }
                else{
                    complain1.setTextColor(getResources().getColor(R.color.cadetBlue));
                }
            }
        });
        complain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(complain2.getCurrentTextColor() == getResources().getColor(R.color.cadetBlue)) {
                    complain2.setTextColor(getResources().getColor(R.color.colorAccent));
                    complain4.setTextColor(getResources().getColor(R.color.cadetBlue));
                    othdetail.setVisibility(View.INVISIBLE);
                }
                else{
                    complain2.setTextColor(getResources().getColor(R.color.cadetBlue));
                }
            }
        });
        complain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complain3.getCurrentTextColor() == getResources().getColor(R.color.cadetBlue)) {
                    complain3.setTextColor(getResources().getColor(R.color.colorAccent));
                    complain4.setTextColor(getResources().getColor(R.color.cadetBlue));
                    othdetail.setVisibility(View.INVISIBLE);
                }
                else{
                    complain3.setTextColor(getResources().getColor(R.color.cadetBlue));
                }
            }
        });
        complain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complain4.setTextColor(getResources().getColor(R.color.colorAccent));
                complain1.setTextColor(getResources().getColor(R.color.cadetBlue));
                complain2.setTextColor(getResources().getColor(R.color.cadetBlue));
                complain3.setTextColor(getResources().getColor(R.color.cadetBlue));

                othdetail.setVisibility(View.VISIBLE);
            }
        });
    }

    public void SendComplain(View view) {

        if(complain1.getCurrentTextColor() == getResources().getColor(R.color.colorAccent) ||
                complain2.getCurrentTextColor() == getResources().getColor(R.color.colorAccent) ||
                complain3.getCurrentTextColor() == getResources().getColor(R.color.colorAccent) ||
                complain4.getCurrentTextColor() == getResources().getColor(R.color.colorAccent)) {
            reference = FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
            HashMap<String, Object> rating = new HashMap<>();
            rating.put("rating", rate);
            reference.updateChildren(rating);

            //For Complain
            reference = FirebaseDatabase.getInstance().getReference("Feedback");
            HashMap<String, Object> complain = new HashMap<>();
            complain.put("complain_id", customerrequestuid);
            complain.put("mechanic_id", mid);
            complain.put("isUser", "yes");
            reference.child(auth.getUid()).setValue(complain);

            Toast.makeText(getApplicationContext(), "Thank you for the feedback to Kaam Wala", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),Home_Navigation.class);
            startActivity(intent);
            finishAffinity();
        }

        if (complain1.getCurrentTextColor() == getResources().getColor(R.color.colorAccent)){
            reference = FirebaseDatabase.getInstance().getReference("Feedback");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Customer Complain 1", "The Worker didn't work correctly");
            reference.child(auth.getUid()).updateChildren(complain1);
        }
        if(complain2.getCurrentTextColor() == getResources().getColor(R.color.colorAccent))
        {
            reference = FirebaseDatabase.getInstance().getReference("Feedback");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Customer Complain 2", "The Worker didn't take care of his hygienes");
            reference.child(auth.getUid()).updateChildren(complain1);
        }
        if(complain3.getCurrentTextColor() == getResources().getColor(R.color.colorAccent))
        {
            reference = FirebaseDatabase.getInstance().getReference("Feedback");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Customer Complain 3", "The Worker was unprofessional");
            reference.child(auth.getUid()).updateChildren(complain1);
        }
        if(complain4.getCurrentTextColor() == getResources().getColor(R.color.colorAccent))
        {
            reference = FirebaseDatabase.getInstance().getReference("Feedback");
            HashMap<String, Object> complain1 = new HashMap<>();
            complain1.put("Customer Complain 4", othdetail.getText().toString());
            reference.child(auth.getUid()).updateChildren(complain1);
        }

        if(complain1.getCurrentTextColor() != getResources().getColor(R.color.colorAccent) &&
                complain2.getCurrentTextColor() != getResources().getColor(R.color.colorAccent) &&
                complain3.getCurrentTextColor() != getResources().getColor(R.color.colorAccent) &&
                complain4.getCurrentTextColor() != getResources().getColor(R.color.colorAccent)){
            Toast.makeText(getApplicationContext(), "Select the Complain", Toast.LENGTH_SHORT).show();
        }
    }
}