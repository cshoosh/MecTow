package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class rating extends AppCompatActivity {
    String discountname ;
    TextView Cname, Kname, catagory, charges;
    RatingBar ratingBar;
    DecimalFormat format;
    FirebaseAuth auth;
    String id;
    DatabaseReference reference;
    double rate, f_rate;
    private FirebaseDatabase database;
    String tooba="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setContentView(R.layout.activity_rate);
        //initialize
        Cname = findViewById(R.id.name_text);
        Kname = findViewById(R.id.name_rate_field);
        catagory = findViewById(R.id.category_field);
        charges = findViewById(R.id.user_rating);
        // ratingBar = findViewById(R.id.simpleRatingBar);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);


        simpleRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            // Called when the user swipes the RatingBar
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String totalStars = "Total Stars:: " + simpleRatingBar.getNumStars();
                String rat = "Rating :: " + simpleRatingBar.getRating();
                Toast.makeText(getApplicationContext(), totalStars + "\n" + rat, Toast.LENGTH_LONG).show();
                database = FirebaseDatabase.getInstance();
                reference = FirebaseDatabase.getInstance().getReference("Complaint");
                reference.child(tooba).child("rat").setValue(rat);
            }
        });


        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Complaint");
        Query q = reference.orderByChild("uid").limitToLast(1);


        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //  Log.d("Firebase", userSnapshot.getKey());
                    tooba = userSnapshot.child("KeyId").getValue(String.class);
                    id = userSnapshot.child("state").getValue(String.class);

                    if(id=="finish")
                    {


                    }
                    //Toast.makeText(getApplicationContext(),  userSnapshot.child("KeyId").getValue(String.class), Toast.LENGTH_LONG).show();
                    // Log.d("Firebase", userSnapshot.child("country").getValue(String.class));
                     Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });




}

}